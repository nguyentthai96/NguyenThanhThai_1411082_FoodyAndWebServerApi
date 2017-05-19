package com.nguyenthanhthai.foodywebapi.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by NguyenThanhThai on 4/27/2017.
 */
@Entity
public class City {

    @Id
    Long id;
    @Column(columnDefinition ="NVARCHAR(255)")
    String nameCity;
    @Column(columnDefinition ="NVARCHAR(255)")
    String displayName;
    @Column(columnDefinition ="NVARCHAR(255)")
    String displayNameEn;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    List<District> districts;

    public City() {
    }

    public City(Long id, String nameCity, String displayName, String displayNameEn) {
        this.id = id;
        this.nameCity = nameCity;
        this.displayName = displayName;
        this.displayNameEn = displayNameEn;
    }

    public Long getId() {
        return id;
    }

    public String getNameCity() {
        return nameCity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayNameEn() {
        return displayNameEn;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDisplayNameEn(String displayNameEn) {
        this.displayNameEn = displayNameEn;
    }
}
