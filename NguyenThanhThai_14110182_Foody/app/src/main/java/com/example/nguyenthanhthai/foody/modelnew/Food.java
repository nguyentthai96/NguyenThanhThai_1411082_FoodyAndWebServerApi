package com.example.nguyenthanhthai.foody.modelnew;

import java.util.List;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */
public class Food {

    Long id;
    String name; //name food
    String photo;
    String photoUrl;
    String ResUrl;

    Long ResPicId;
    String ResUrlRewriteName;
    String ResLocation;


    List<Feedback> feedbacks;
}
