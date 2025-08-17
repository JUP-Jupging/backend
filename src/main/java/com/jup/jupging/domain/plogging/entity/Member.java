package com.jup.jupging.domain.plogging.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_gen")
    @SequenceGenerator(name = "member_seq_gen", sequenceName = "member_seq", allocationSize = 1)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "app_nickname")
    private String appNickname;

    @Column(name = "provider")
    private String provider;

    @Column(name = "is_deleted", length = 1, nullable = false)
    private String isDeleted; // "Y" 또는 "N"

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "activity_region")
    private String activityRegion;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

