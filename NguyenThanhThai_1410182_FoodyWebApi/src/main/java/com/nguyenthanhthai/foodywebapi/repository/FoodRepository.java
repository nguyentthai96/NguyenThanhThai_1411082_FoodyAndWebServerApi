package com.nguyenthanhthai.foodywebapi.repository;

import com.nguyenthanhthai.foodywebapi.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */
public interface FoodRepository extends JpaRepository<Food,Long>{
}
