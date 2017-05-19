package com.nguyenthanhthai.foodywebapi.controller;

import com.nguyenthanhthai.foodywebapi.model.Feedback;
import com.nguyenthanhthai.foodywebapi.model.City;
import com.nguyenthanhthai.foodywebapi.model.User;
import com.nguyenthanhthai.foodywebapi.repository.CityRepository;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */

@RestController
@RequestMapping("/api/city")
@Api(name = "City API", description = "Provider a database City", stage = ApiStage.RC)
public class CityController {

    CityRepository cityRepository;

    @Value("${folder_resource_json_city}")
    private String jsonCity;

    @Autowired
    public CityController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @RequestMapping("/readfileJson/{country}")
    @ApiMethod(description = "Insert database form Json file.. Return list")
    public List<City> readFileJsonCreateDatabase(@ApiPathParam(name = "country") @PathVariable(name = "country") String country) {
       String filePath = jsonCity+country+".json";
        readDataFromFileJson(filePath);
        return cityRepository.findAll();
    }

    private void readDataFromFileJson(String filePath) {
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader(filePath));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray dataList = (JSONArray) jsonObject.get("data");
            for (Object itemData : dataList.toArray()) {
                JSONObject jsonItemData = (JSONObject) itemData;

                City city = new City(
                        (Long) jsonItemData.get("Id"),
                        (String) jsonItemData.getOrDefault("Name", null),
                        (String) jsonItemData.getOrDefault("DisplayName", null),
                        (String) jsonItemData.getOrDefault("DisplayNameEn", null));


                //Save city to database
                cityRepository.save(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiMethod(description = "Api create element")
    public boolean create(@RequestBody City city) {
        cityRepository.save(city);

        return true;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ApiMethod(description = "Api delete element by id")
    public boolean delete(@ApiPathParam(name = "id") @PathVariable long id) {
        cityRepository.delete(id);
        return true;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @ApiMethod(description = "Api edit element by id")
    public boolean edit(@ApiPathParam(name = "id") @PathVariable long id, @RequestBody City cityNew) {
        City city = new City();
        cityRepository.save(city);
        return true;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiMethod(description = "Get all restarant... where")
    public List<City> getAll() {
        return cityRepository.findAll();
    }

    @RequestMapping(value = "/find-by-id/{id}", method = RequestMethod.GET)
    @ApiMethod(description = "Api find element by id")
    public City getById(@ApiPathParam(name = "id") @PathVariable long id) {
        return cityRepository.findOne(id);
    }
}
