package com.nguyenthanhthai.foodywebapi.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by NguyenThanhThai on 4/27/2017.
 */
@Entity
public class CategoryType{

    @Id
    Long id;
    @Column(columnDefinition ="NVARCHAR(255)")
    String name;
    @Column(columnDefinition ="NVARCHAR(255)")
    String imageBackgroud;
    @Column(columnDefinition ="NVARCHAR(255)")
    String colorBackgroud;

    @OneToMany(mappedBy = "categoryType", cascade = CascadeType.ALL)
    List<Category> categories;

    public CategoryType() {
    }

    public CategoryType(Long id, String name, String imageBackgroud, String colorBackgroud) {
        this.id = id;
        this.name = name;
        this.imageBackgroud = imageBackgroud;
        this.colorBackgroud = colorBackgroud;
    }
}
