package com.nguyenthanhthai.foodywebapi.repository;

import com.nguyenthanhthai.foodywebapi.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.*;
import java.util.List;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    @Query(value = "SELECT *, (6371 * acos(cos(radians(:#{#myPoint.x})) *  cos(radians(latitude)) *  cos(radians(longtitude) -  radians(:#{#myPoint.y})) +  sin(radians(:#{#myPoint.x})) *  sin(radians(latitude)))) as distance FROM foody.restaurant HAVING (distance < :#{#range}) ORDER BY distance LIMIT :#{#limitStart}, :#{#limitEnd}", nativeQuery = true)
    List<Restaurant> selectRetaurntNear(@Param("myPoint")  Point.Double myPoint,@Param("range") Double range,@Param("limitStart") Long limitStart,@Param("limitEnd") Long limitEnd);
}
