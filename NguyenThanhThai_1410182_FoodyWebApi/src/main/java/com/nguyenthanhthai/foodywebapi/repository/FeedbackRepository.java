package com.nguyenthanhthai.foodywebapi.repository;

import com.nguyenthanhthai.foodywebapi.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */
public interface FeedbackRepository extends JpaRepository<Feedback,Long>{
}
