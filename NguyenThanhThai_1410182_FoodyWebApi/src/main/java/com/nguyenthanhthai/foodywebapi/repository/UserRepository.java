package com.nguyenthanhthai.foodywebapi.repository;

import com.nguyenthanhthai.foodywebapi.model.Restaurant;
import com.nguyenthanhthai.foodywebapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */
public interface UserRepository extends JpaRepository<User,Long>{
}
