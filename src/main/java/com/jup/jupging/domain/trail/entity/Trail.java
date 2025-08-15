package com.jup.jupging.domain.trail.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "trail")
public class Trail {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trail_seq_gen")
    @SequenceGenerator(name = "trail_seq_gen", sequenceName = "trail_seq", allocationSize = 1)
    @Column(name = "trail_id")
    private Long trailId;

    @Column(name = "trail_type_name", length = 200)
    private String trailTypeName;

    @Column(name = "trail_name", length = 200)
    private String trailName;

    // default로 String 타입을 VARCHAR(255)로 지정해서 수동 지정해줘야 한다
    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "city_name", length = 200)
    private String cityName;

    @Column(name = "difficulty_level", length = 100)
    private String difficultyLevel;

    @Column(name = "length", length = 200)
    private String length;

    @Column(name = "length_detail")
    private double lengthDetail;

    @Lob
    @Column(name = "description_detail")
    private String descriptionDetail;

    @Column(name = "track_time", length = 100)
    private String trackTime;

    @Column(name = "option_description", length = 200)
    private String optionDescription;

    @Column(name = "toilet_description", length = 200)
    private String toiletDescription;

    @Column(name = "amenity_description", length = 200)
    private String amenityDescription;

    @Column(name = "lot_number_address", length = 200)
    private String lotNumberAddress;

    @Column(name = "spot_latitude")
    private double spotLatitude;

    @Column(name = "spot_longitude")
    private double spotLongitude;

    @Column(name = "report_count", columnDefinition = "INT DEFAULT 0")
    private int reportCount;

    @Column(name = "img1")
    private String imageUrl1;

    @Column(name="img2")
    private String imageUrl2;


    public Trail(String trailTypeName, String trailName, String description, String cityName, String difficultyLevel, String length, double lengthDetail, String descriptionDetail,String trackTime, String optionDescription, String toiletDescription, String amenityDescription, String lotNumberAddress, double spotLatitude, double spotLongitude, int reportCount) {
        this.trailTypeName = trailTypeName;
        this.trailName = trailName;
        this.description = description;
        this.cityName = cityName;
        this.difficultyLevel = difficultyLevel;
        this.length = length;
        this.lengthDetail = lengthDetail;
        this.descriptionDetail = descriptionDetail;
        this.trackTime = trackTime;
        this.optionDescription = optionDescription;
        this.toiletDescription = toiletDescription;
        this.amenityDescription = amenityDescription;
        this.lotNumberAddress = lotNumberAddress;
        this.spotLatitude = spotLatitude;
        this.spotLongitude = spotLongitude;
        this.reportCount = reportCount;
    }
}
