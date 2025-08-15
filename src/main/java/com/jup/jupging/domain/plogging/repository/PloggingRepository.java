package com.jup.jupging.domain.plogging.repository;

import com.jup.jupging.domain.plogging.entity.Plogging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PloggingRepository extends JpaRepository<Plogging, Long> {
    @Query("SELECT p FROM Plogging p WHERE p.member.memberId = :memberId AND p.ploggingTime IS NULL")
    Optional<Plogging> findActivePloggingForUpdate(@Param("memberId") Long memberId);

    @Query("SELECT p FROM Plogging p WHERE p.member.memberId = :memberId AND p.ploggingId = :ploggingId")
    Plogging findByIdAndMember_MemberId(Long ploggingId, Long memberId);
}
