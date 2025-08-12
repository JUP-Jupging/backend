package com.jup.jupging.domain.trail.repository;

import com.jup.jupging.domain.trail.dto.TrailWithDistanceDto;
import com.jup.jupging.domain.trail.entity.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrailRepository extends JpaRepository<Trail, Long>, JpaSpecificationExecutor<Trail> {

    @Query("SELECT t FROM Trail t WHERE t.trailTypeName LIKE %:keyword% OR t.trailName LIKE %:keyword% OR t.cityName LIKE %:keyword%")
    List<Trail> findAllByTrailTypeNameOrTrailNameOrCityName(String keyword);

    @Query(value = """
        SELECT
        t.trail_id,
        t.trail_type_name,
        t.trail_name,
        t.city_name,
        t.difficulty_level,
        t.length,
        t.length_detail,
        t.track_time,
        t.lot_number_address,
        t.spot_latitude,
        t.spot_longitude,
        t.report_count,
         (6371 * ACOS(
         COS(t.spot_latitude * 3.1415926535 / 180) * COS(:userLat * 3.1415926535 / 180) *
         COS((:userLong - t.spot_longitude) * 3.1415926535 / 180) +
         SIN(t.spot_latitude * 3.1415926535 / 180) * SIN(:userLat * 3.1415926535 / 180)
         )) AS distanceToUser
        FROM trail t
        WHERE (6371 * ACOS(
             COS(t.spot_latitude * 3.1415926535 / 180) * COS(:userLat * 3.1415926535 / 180) *
             COS((:userLong - t.spot_longitude) * 3.1415926535 / 180) +
             SIN(t.spot_latitude * 3.1415926535 / 180) * SIN(:userLat * 3.1415926535 / 180)
         )) <= 5
        ORDER BY distanceToUser""", nativeQuery = true)
    List<Object[]> findTrailsNearby(double userLat, double userLong);

}
