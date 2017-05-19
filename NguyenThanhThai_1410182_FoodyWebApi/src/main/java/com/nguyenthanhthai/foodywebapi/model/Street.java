package com.nguyenthanhthai.foodywebapi.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by NguyenThanhThai on 4/27/2017.
 */
@Entity
public class Street{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition ="NVARCHAR(255)")
    private String name;
    //Name chua xu ly
    @Column(columnDefinition ="NVARCHAR(255)")
    String nameOld;

    @ManyToOne
    private District district;

    public Street() {
    }

    public Street(String name, String nameOld, District district) {
        this.name = name;
        this.nameOld = nameOld;
        this.district = district;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOld() {
        return nameOld;
    }

    public void setNameOld(String nameOld) {
        this.nameOld = nameOld;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
