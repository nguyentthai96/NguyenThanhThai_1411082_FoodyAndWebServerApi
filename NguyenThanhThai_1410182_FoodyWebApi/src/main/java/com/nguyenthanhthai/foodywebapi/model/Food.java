package com.nguyenthanhthai.foodywebapi.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */
@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(columnDefinition ="NVARCHAR(255)")
    String name; //name food
    @Column(columnDefinition ="NVARCHAR(255)")
    String photo;
    @Column(columnDefinition ="NVARCHAR(255)")
    String photoUrl;
    @Column(columnDefinition ="NVARCHAR(255)")
    String ResUrl;

    Long ResPicId;
    @Column(columnDefinition ="NVARCHAR(255)")
    String ResUrlRewriteName;
    @Column(columnDefinition ="NVARCHAR(255)")
    String ResLocation;


    @OneToMany
    List<Feedback> feedbacks;
}
