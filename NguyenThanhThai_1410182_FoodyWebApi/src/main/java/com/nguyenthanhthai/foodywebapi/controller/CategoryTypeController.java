package com.nguyenthanhthai.foodywebapi.controller;

import com.nguyenthanhthai.foodywebapi.model.CategoryType;
import com.nguyenthanhthai.foodywebapi.repository.CategoryTypeRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */

@RestController
@RequestMapping("/api/categoryType")
@Api(name = "CategoryType API", description = "Provider a database CategoryType", stage = ApiStage.RC)
public class CategoryTypeController {

    CategoryTypeRepository categoryTypeRepository;

    @Autowired
    public CategoryTypeController(CategoryTypeRepository categoryTypeRepository) {
        this.categoryTypeRepository = categoryTypeRepository;
    }

    @RequestMapping("/initialize")
    @ApiMethod(description = "Insert database.. Return list")
    public List<CategoryType> initializeDatabase() {

        List<CategoryType> categoryTypes=new ArrayList<>();
        categoryTypes.add(new CategoryType(1L,"Ăn uống","icon_anuong","#ffff0000"));
        categoryTypes.add(new CategoryType(1L,"Du lịch","icon_dulich","#ff0855c7"));
        categoryTypes.add(new CategoryType(1L,"Cưới hỏi","icon_cuoihoi","#ffff4081"));
        categoryTypes.add(new CategoryType(1L,"Đẹp khỏe","icon_lamdep","#ffff6e6e"));
        categoryTypes.add(new CategoryType(1L,"Giải trí","icon_giaitri","#ffcddc39"));
        categoryTypes.add(new CategoryType(1L,"Mua sắm","icon_muasam","#ffff9800"));
        categoryTypes.add(new CategoryType(1L,"Giáo dục","icon_giaoduc","#ff4caf50"));
        categoryTypes.add(new CategoryType(1L,"Dịch vụ","icon_dich_vu","#ff607d8b"));



        return categoryTypeRepository.findAll();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiMethod(description = "Api create element")
    public boolean create(@RequestBody CategoryType categoryType) {
        categoryTypeRepository.save(categoryType);

        return true;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ApiMethod(description = "Api delete element by id")
    public boolean delete(@ApiPathParam(name = "id") @PathVariable long id) {
        categoryTypeRepository.delete(id);
        return true;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @ApiMethod(description = "Api edit element by id")
    public boolean edit(@ApiPathParam(name = "id") @PathVariable long id, @RequestBody CategoryType categoryTypeNew) {
        CategoryType categoryType = new CategoryType();
        categoryTypeRepository.save(categoryType);
        return true;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiMethod(description = "Get all restarant... where")
    public List<CategoryType> getAll() {
        return categoryTypeRepository.findAll();
    }

    @RequestMapping(value = "/find-by-id/{id}", method = RequestMethod.GET)
    @ApiMethod(description = "Api find element by id")
    public CategoryType getById(@ApiPathParam(name = "id") @PathVariable long id) {
        return categoryTypeRepository.findOne(id);
    }
}
