package com.nguyenthanhthai.foodywebapi.model;
import javax.persistence.*;
import java.util.List;

/**
 * Created by NguyenThanhThai on 4/27/2017.
 */
@Entity
public class District {
    @Id
    Long id;
    @Column(columnDefinition ="NVARCHAR(255)")
    String name;

    @ManyToOne
    City city;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    List<Street> streets;


    public District() {
    }

    public District(Long id, String name, City city) {
        this.id = id;
        this.name = name;
        this.city = city;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
