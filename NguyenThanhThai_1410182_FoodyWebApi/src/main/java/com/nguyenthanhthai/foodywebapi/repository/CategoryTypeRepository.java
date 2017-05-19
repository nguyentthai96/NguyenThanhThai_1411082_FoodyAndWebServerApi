package com.nguyenthanhthai.foodywebapi.repository;

import com.nguyenthanhthai.foodywebapi.model.CategoryType;
import com.nguyenthanhthai.foodywebapi.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */
public interface CategoryTypeRepository extends JpaRepository<CategoryType,Long>{
}
