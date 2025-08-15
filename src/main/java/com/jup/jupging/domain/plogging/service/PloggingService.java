package com.jup.jupging.domain.plogging.service;

import com.jup.jupging.domain.plogging.entity.Plogging;
import com.jup.jupging.domain.plogging.entity.PloggingTrash;
import com.jup.jupging.domain.plogging.dto.PloggingDto;
import com.jup.jupging.domain.plogging.dto.PloggingRequestDto;
import com.jup.jupging.domain.plogging.repository.MemberRepository;
import com.jup.jupging.domain.plogging.repository.PloggingRepository;
import com.jup.jupging.domain.plogging.repository.PloggingTrashRepository;
import com.jup.jupging.domain.trail.repository.TrailRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PloggingService {
    private final MemberRepository memberRepository;
    private final TrailRepository trailRepository;
    private final PloggingRepository ploggingRepository;
    private final PloggingTrashRepository ploggingTrashRepository;

    public PloggingService(MemberRepository memberRepository, TrailRepository trailRepository, PloggingRepository ploggingRepository, PloggingTrashRepository ploggingTrashRepository) {
        this.memberRepository = memberRepository;
        this.trailRepository = trailRepository;
        this.ploggingRepository = ploggingRepository;
        this.ploggingTrashRepository = ploggingTrashRepository;
    }


    // 플로깅 중일 때 쓰레기 줍기 요청 시
    @Transactional
    public String insertPickedTrash(Long ploggingId,Long reportId){

        // 제보 내역에 플로깅 ID가 null인 상태엿다가 플로깅할 때 쓰레기 줍기할 때 들어가게( 한 사람 당 플로깅(산책로) 하나만 한다고 가정 )
        PloggingTrash trash = ploggingTrashRepository.getReferenceById(reportId);
        System.out.println("trash = " + trash);
        trash.setIsPicked("y");
        trash.setPlogging(ploggingRepository.getReferenceById(ploggingId));

        return "쓰레기 줍기 성공";

    }

    @Transactional
    public void insertPlopping(PloggingRequestDto plogging) {

        //플로깅 시작할 때 작업임
        if(plogging.isStart()){
            Optional<Plogging> activePlogging = ploggingRepository.findActivePloggingForUpdate(plogging.getMemberId());
            if (activePlogging.isPresent()) {
                throw new IllegalArgumentException("이미 플로깅 중입니다. 먼저 종료하고 시도해주세요.");
            }

            Plogging p = new Plogging(
                    memberRepository.getReferenceById(plogging.getMemberId()),
                    trailRepository.getReferenceById(plogging.getTrailId()),
                    LocalDate.now(),
                    null,
                    0.0,
                    plogging.getStartLat(),
                    plogging.getStartLng(),
                    null
            );
            ploggingRepository.save(p);
        }

        //플로깅 끝날 때 작업
        else {
            Plogging pl = ploggingRepository.findActivePloggingForUpdate(plogging.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("진행 중인 플로깅이 없습니다."));
            // 나머지
            pl.setDistance(plogging.getDistance());
            pl.setPloggingTime(plogging.getPloggingTime());
            ploggingRepository.save(pl);
        }
    }

    public PloggingDto getPlogging(Long ploggingId, Long memberId) {
        Plogging plogging = ploggingRepository.findByIdAndMember_MemberId(ploggingId, memberId);
        return new PloggingDto(plogging);
    }
}
