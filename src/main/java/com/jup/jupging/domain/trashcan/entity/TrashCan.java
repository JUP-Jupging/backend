package com.jup.jupging.domain.trashcan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "trash_can")
public class TrashCan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trash_can_seq_gen")
    @SequenceGenerator(name = "trash_can_seq_gen", sequenceName = "trash_can_seq", allocationSize = 1)
    @Column(name = "trash_can_id")
    private Long trashCanId;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "lot_number_address")
    private String lotNumberAddress;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "trash_can_type")
    private String trashCanType;

    @Column(name = "institution")
    private String institution;

    @Column(name = "institution_tel")
    private String institutionTel;

}