package com.nguyenthanhthai.foodywebapi.repository;

import com.nguyenthanhthai.foodywebapi.model.City;
import com.nguyenthanhthai.foodywebapi.model.Street;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */
public interface StreetRepository extends JpaRepository<Street,Long>{

    Street findByName(String name);
}
