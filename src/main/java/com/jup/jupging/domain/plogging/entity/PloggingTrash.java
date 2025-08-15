package com.jup.jupging.domain.plogging.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jup.jupging.domain.trail.entity.Trail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "report")
@Setter@Getter
public class PloggingTrash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plogging_id")
    @JsonIgnore
    private Plogging plogging;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trail_id")
    @JsonIgnore
    private Trail trail;

    @Column(name = "lat", nullable = false)
    private Double lat;

    @Column(name = "lng", nullable = false)
    private Double lng;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "image_url", nullable = false, length = 1000)
    private String imageUrl;

    @Column(name = "paper", nullable = false)
    private Integer paper = 0;

    @Column(name = "can", nullable = false)
    private Integer can = 0;

    @Column(name = "plastic", nullable = false)
    private Integer plastic = 0;

    @Column(name = "vinyl", nullable = false)
    private Integer vinyl = 0;

    @Column(name = "glass", nullable = false)
    private Integer glass = 0;

    @Column(name = "styro", nullable = false)
    private Integer styro = 0;

    @Column(name = "battery", nullable = false)
    private Integer battery = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "is_picked", nullable = false, length = 1) // y / n
    private String isPicked = "N";

}
