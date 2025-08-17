package com.jup.jupging.domain.plogging.entity;

import com.jup.jupging.domain.trail.entity.Trail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plogging")
@NoArgsConstructor
@Setter@Getter
public class Plogging {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plogging_seq_gen")
    @SequenceGenerator(name = "plogging_seq_gen", sequenceName = "plogging_seq", allocationSize = 1)
    private Long ploggingId;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "trail_id")
    private Trail trail;
    @Column(name = "plogging_date")
    private LocalDate ploggingDate;
    @Column(name = "plogging_time")
    private String ploggingTime;
    @Column
    private Double distance;
    @Column(name = "start_lat")
    private Double startLat;
    @Column(name = "start_lng")
    private Double startLng;
    @Column(name = "image_url")
    private String imageUrl;

    public Plogging(Member member, Trail trail, LocalDate ploggingDate, String ploggingTime, Double distance, Double startLat, Double startLng, String imageUrl) {
        this.member = member;
        this.trail = trail;
        this.ploggingDate = ploggingDate;
        this.ploggingTime = ploggingTime;
        this.distance = distance;
        this.startLat = startLat;
        this.startLng = startLng;
    }

    // 참조
    @OneToMany(mappedBy = "plogging", cascade = CascadeType.ALL)
    private List<PloggingTrash> trashList = new ArrayList<>();
    public void setTrashList(List<PloggingTrash> list) {
        this.trashList.clear();
        if (list != null) {
            this.trashList.addAll(list);
        }
    }


}
