package com.nguyenthanhthai.foodywebapi.model;

import javax.persistence.*;

/**
 * Created by NguyenThanhThai on 4/27/2017.
 */
@Entity
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(columnDefinition ="NVARCHAR(255)")
    String name;
    @Column(columnDefinition ="NVARCHAR(255)")
    String img;

    @ManyToOne
    CategoryType categoryType;

    public Category() {
    }
}
