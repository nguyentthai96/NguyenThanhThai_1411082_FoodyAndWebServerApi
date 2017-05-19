package com.nguyenthanhthai.foodywebapi.controller;

import com.nguyenthanhthai.foodywebapi.model.*;
import com.nguyenthanhthai.foodywebapi.repository.CityRepository;
import com.nguyenthanhthai.foodywebapi.repository.DistrictRepository;
import com.nguyenthanhthai.foodywebapi.repository.FoodRepository;
import javafx.util.Pair;
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

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by NguyenThanhThai on 4/28/2017.
 */

@RestController
@RequestMapping("/api/food")
@Api(name = "Food API", description = "Provider a database Food", stage = ApiStage.RC)
public class FoodController {

    FoodRepository foodRepository;
    CityRepository cityRepository;
    DistrictRepository districtRepository;
    @Value("${folder_resource_image_where}")
    private String imageWhere;

    @Value("${folder_resource_json_where}")
    private String jsonWhere;

    @Autowired
    public FoodController(FoodRepository foodRepository,
                          CityRepository cityRepository, DistrictRepository districtRepository) {
        this.foodRepository = foodRepository;
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
    }

    @RequestMapping("/readAllFileJson")
    @ApiMethod(description = "Insert database form Json file.. Return list")
    public boolean readAllFileJson() {
        File folder = new File(jsonWhere);
        Arrays.stream(folder.listFiles()).forEach(f -> {
            if (f.isFile()) {
                String name = f.getName();

                Pattern p = Pattern.compile("-?\\d+");
                Matcher m = p.matcher(name);
                m.find();
                Long typeId = new Long(m.group());
                m.find();
                Long cateId = new Long(m.group());
                m.find();
                Long cityId = new Long(m.group());
                m.find();
                Long disId = new Long(m.group());

                readDataFromFileJson(jsonWhere + name, typeId, cateId, cityId, disId);
            }
        });
        return true;
    }

    @RequestMapping("/readfileJson/{typeId}/{cateId}/{cityId}/{disId}/{numberFile}")
    @ApiMethod(description = "Insert database form Json file.. Return list")
    public List<Food> readFileJsonCreateDatabase(@ApiPathParam(name = "typeId") @PathVariable(name = "typeId") Long typeId,
                                                       @ApiPathParam(name = "cateId") @PathVariable(name = "cateId") Long cateId,
                                                       @ApiPathParam(name = "cityId") @PathVariable(name = "cityId") Long cityId,
                                                       @ApiPathParam(name = "disId") @PathVariable(name = "disId") Long disId,
                                                       @ApiPathParam(name = "numberFile") @PathVariable(name = "numberFile") Integer numberFile) {
        List<Food> foods = new ArrayList<>();

        for (int i = 0; i < numberFile; i++) {
            String filePath = jsonWhere
                    + "type" + typeId + "cate" + cateId + "city" + cityId + "dis" + disId + "num" + i + ".json";
            foods.addAll(readDataFromFileJson(filePath, typeId, cateId, cityId, disId));
        }
        return foods;
    }

    private List<Food> readDataFromFileJson(String filePath, Long typeId, Long cateId, Long cityId, Long disId) {
        List<Food> foods = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader(filePath));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray dataList = (JSONArray) jsonObject.get("data");
            for (Object itemData : dataList.toArray()) {
                JSONObject jsonItemData = (JSONObject) itemData;

                //TODO
//                Food food = new Food(
//                        (Long) jsonItemData.get("FoodID"),
//                        (String) jsonItemData.getOrDefault("Name", null),
//                        (String) jsonItemData.getOrDefault("Name_EN", null),
//                        (String) jsonItemData.getOrDefault("Address", null),
//                        Double.parseDouble(jsonItemData.get("Latitude").toString()),
//                        Double.parseDouble(jsonItemData.get("Longtitude").toString()),
//                        (String) jsonItemData.getOrDefault("Phone", null),
//                        (String) jsonItemData.getOrDefault("MobileImageUrl", null),
//                        (String) jsonItemData.getOrDefault("PhotoUrl", null),
//                        (Long) jsonItemData.get("TotalReviews"),
//                        (Long) jsonItemData.get("TotalPictures"),
//                        (Long) jsonItemData.get("TotalViews"),
//                        (Double) jsonItemData.get("AvgRating"),
//                        (String) jsonItemData.getOrDefault("LocationUrlRewriteName", null));


                // Get list feedback for food, two feeedback
                JSONArray feedbackList = (JSONArray) jsonItemData.get("TopReViews");
                List<Feedback> feedbacks = new ArrayList<>();
                for (Object itemFeedback : feedbackList.toArray()) {
                    JSONObject jsonItemFeedback = (JSONObject) itemFeedback;
                    Feedback feedback = new Feedback(
                            (Long) jsonItemFeedback.get("ReviewID"),
                            (Long) jsonItemFeedback.get("FoodID"),
                            (String) jsonItemFeedback.getOrDefault("Comment", null),
                            (String) jsonItemFeedback.getOrDefault("Title", null),
                            (Double) jsonItemFeedback.getOrDefault("AvgRating", 0));
                    User user = new User(
                            (Long) jsonItemFeedback.get("UserID"),
                            (String) jsonItemFeedback.getOrDefault("Username", null),
                            (String) jsonItemFeedback.getOrDefault("Email", null),
                            (String) jsonItemFeedback.getOrDefault("FullName", null),
                            (String) jsonItemFeedback.getOrDefault("OwnerFirstName", null),
                            (String) jsonItemFeedback.getOrDefault("OwnerLastName", null),
                            (String) jsonItemFeedback.getOrDefault("AvatarURL", null),
                            jsonItemFeedback.getOrDefault("Gender", "F") == null ? false :
                                    ((((String) jsonItemFeedback.getOrDefault("Gender", "F")).equals("F")) ? false : true));
                    feedback.setUser(user);
                    feedbacks.add(feedback);
                }
                //TODO
//                //list feedback
//                food.setFeedbacks(feedbacks);
//
//                food.setCategoryTypeId(typeId);
//                food.setCategoryId(cateId);
//                String address = "chuacoten";
//
//                Pair<String, Pair<Double, Double>> stringPairPair = getLatitudeLongtitude(food.getAddress());
//
//                try {
//                    food.setLatitude(stringPairPair.getValue().getKey());
//                    food.setLongtitude(stringPairPair.getValue().getValue());
//                } catch (Exception e) {
//
//                }
//
//                if (stringPairPair.getKey() == null) {
//                        //Resolve Street //TODO
//                        address = food.getAddress();
//                        address = address.trim();
//                        address = address.substring(0, address.indexOf(", Quận"));
//                        if (address.contains(", P.")) {
//                            address = address.substring(0, address.indexOf(", P."));
//                        }
//                        if (address.contains(", Phường")) {
//                            address = address.substring(0, address.indexOf(", Phường"));
//                        }
//                        //Remove prefix
//                        address = address.substring(address.indexOf(" "), address.length());
//                } else {
//                    address = stringPairPair.getKey();
//                }
//
//                District district = new District(disId, "District", cityRepository.findOne(cityId));
//                districtRepository.save(district);
//                food.setStreet(new Street(address, food.getAddress(), district));
//
//                try {
//                    //Save food to database
//                    foodRepository.save(food);
//                    foods.add(food);
//                } catch (Exception e) {
//                    food.getFeedbacks().get(0).setComment(food.getFeedbacks().get(0).getComment().substring(0, 60));
//                    food.getFeedbacks().get(1).setComment(food.getFeedbacks().get(0).getComment().substring(0, 60));
//                    //Save food to database
//                    foodRepository.save(food);
//                    foods.add(food);
//                }
//            }
        }} catch (Exception e) { //TODO }
            e.printStackTrace();
        }

        return foods;
    }

    /**
     * Return street,(Pair<>(latitude, longitude))
     */
    public static Pair<String, Pair<Double, Double>> getLatitudeLongtitude(String accessToken) throws Exception {//GET Method
        final URL url = new URL("http://maps.google.com/maps/api/geocode/" + "json?address=" + URLEncoder.encode(accessToken) + "&sensor=false");

        Double latitude = 0D, longitude = 0D;
        String street = null;

        InputStream inputStream = null;
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        if (con.getResponseCode() != 200) {
            return null;
        }

        boolean StreetGeted = false;
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        try {

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("\"location\" : {")) {
                    if ((inputLine = in.readLine()) != null) {
                        latitude = Double.parseDouble(inputLine.substring(23, inputLine.length() - 1));
                    }
                    if ((inputLine = in.readLine()) != null) {
                        longitude = Double.parseDouble(inputLine.substring(23, inputLine.length()));
                        break;
                    }
                } else {
                    if (!StreetGeted && inputLine.contains("\"long_name\" : ")) {
                        street = (inputLine.substring(30, inputLine.length() - 2));
                    }
                    if (inputLine.contains("\"types\" : [ \"route\" ]")) {
                        StreetGeted = true;
                    }
                }
            }
        } catch (Exception e) {

        }

        in.close();
        if (StreetGeted)
            return new Pair<>(street, new Pair<>(latitude, longitude));
        else {
            return new Pair<>(null, new Pair<>(latitude, longitude));
        }
    }

    @RequestMapping("/downloadFileAllFromUrl")
    @ApiMethod(description = "Dowload all image form url in database")
    public boolean downloadFileAllFromUrl() {
//        List<Food> foods = foodRepository.findAll();
//
//        for (Food food : foods) {
//            String toFile = imageWhere + food.getMobileImageUrl();
//
//            try {
//                //connectionTimeout, readTimeout = 10 seconds
//                FileUtils.copyURLToFile(new URL(food.getPhotoUrl()), new File(toFile), 10000, 10000);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println(food.toString());
//                return false;
//            }
//
//
//            List<Feedback> feedbacks = food.getFeedbacks();
//            for (Feedback item : feedbacks) {
//                toFile = imageWhere + "avatar" + item.getUser().getUserId() + ".jpg";
//
//                try {
//                    //connectionTimeout, readTimeout = 10 seconds
//                    FileUtils.copyURLToFile(new URL(item.getUser().getAvatarURL()), new File(toFile), 10000, 10000);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return false;
//                }
//            }
//        }
        return true;
    }

    @RequestMapping("/resizeFileAllImageFromFolder")
    @ApiMethod(description = "Resize all image form folder")
    public boolean resizeFileAllImageFromFolder() throws IOException {
        Thumbnails.of(new File(imageWhere).listFiles())
                .scale(0.8)
                .outputFormat("jpg")
                .toFiles(Rename.NO_CHANGE);
        return true;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiMethod(description = "Api create element")
    public boolean create(@RequestBody Food food) {
        foodRepository.save(food);

        return true;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ApiMethod(description = "Api delete element by id")
    public boolean delete(@ApiPathParam(name = "id") @PathVariable long id) {
        foodRepository.delete(id);
        return true;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @ApiMethod(description = "Api edit element by id")
    public boolean edit(@ApiPathParam(name = "id") @PathVariable long id, @RequestBody Food foodNew) {
        Food food = new Food();
        foodRepository.save(food);
        return true;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiMethod(description = "Get all restarant... where size")
    public Long getAll() {
        return new Long(foodRepository.findAll().size());
    }

    @RequestMapping(value = "/find-by-id/{id}", method = RequestMethod.GET)
    @ApiMethod(description = "Api find element by id")
    public Food getById(@ApiPathParam(name = "id") @PathVariable long id) {
        return foodRepository.findOne(id);
    }

    @RequestMapping(value = "/image/{mobileImageUrl:.+}", produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.GET)
    @ApiMethod(description = "View one image with name mobileImageUrl")
    public @ResponseBody
    ResponseEntity<Resource> downloadFile(@ApiPathParam(name = "mobileImageUrl") @PathVariable String mobileImageUrl) {
        String fromFile = imageWhere + mobileImageUrl;
        File file = FileUtils.getFile(fromFile);
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<Resource>(fileSystemResource, headers, HttpStatus.OK);
    }


    public static boolean pointIsInCircle(Point.Double pointForCheck, Point.Double center, double radius) {
        if (getDistanceBetweenTwoPoints(pointForCheck, center) <= radius)
            return true;
        else
            return false;
    }

    public static double getDistanceBetweenTwoPoints(Point.Double p1, Point.Double p2) {
        double R = 6371000; // m
        double dLat = Math.toRadians(p2.x - p1.x);
        double dLon = Math.toRadians(p2.y - p1.y);
        double lat1 = Math.toRadians(p1.x);
        double lat2 = Math.toRadians(p2.x);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
                * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        return d;
    }


    @RequestMapping(value = "/fielter/{typeId}/{cateId}/{cityId}/{disId}/{streetId}/{limitStart}/{limitEnd}/", method = RequestMethod.GET)
    @ApiMethod(description = "View  fielter Retaurant")
    private List<Food> fieterRetaurant(@ApiPathParam(name = "typeId") @PathVariable(name = "typeId") Long typeId,
                                             @ApiPathParam(name = "cateId") @PathVariable(name = "cateId") Long cateId,
                                             @ApiPathParam(name = "cityId") @PathVariable(name = "cityId") Long cityId,
                                             @ApiPathParam(name = "disId") @PathVariable(name = "disId") Long disId,
                                             @ApiPathParam(name = "streetId") @PathVariable(name = "streetId") Long streetId,
                                             @ApiPathParam(name = "limitStart") @PathVariable("limitStart") Long limitStart,
                                             @ApiPathParam(name = "limitEnd") @PathVariable("limitEnd") Long limitEnd) {

//        List<Food> foods = foodRepository.findAll();
//        List<Food> foodFielters;
//
//        typeId = 1L;
//        if (cateId == 0) {
//            if (disId == 0) {
//                if (streetId == 0) {
//                    return foods.subList(limitStart.intValue(), limitEnd.intValue());
//                } else {
//                    foodFielters = foods.stream().filter(r -> (r.getStreet().getId() == streetId)).skip(limitStart).limit(limitEnd).collect(Collectors.toList());
//                }
//            } else {
//                if (streetId == 0) {
//                    foodFielters = foods.stream().filter(r -> (r.getStreet().getDistrict().getId() == disId)).skip(limitStart).limit(limitEnd).collect(Collectors.toList());
//                } else {
//                    foodFielters = foods.stream().filter(r -> (r.getStreet().getDistrict().getId() == disId && r.getStreet().getId() == streetId)).skip(limitStart).limit(limitEnd).collect(Collectors.toList());
//                }
//            }
//        } else {
//            if (disId == 0) {
//                if (streetId == 0) {
//                    foodFielters = foods.stream().filter(r -> (r.getCategoryId() == cateId)).skip(limitStart).limit(limitEnd).collect(Collectors.toList());
//                } else {
//                    foodFielters = foods.stream().filter(r -> (r.getCategoryId() == cateId && r.getStreet().getId() == streetId)).skip(limitStart).limit(limitEnd).collect(Collectors.toList());
//                }
//            } else {
//                if (streetId == 0) {
//                    foodFielters = foods.stream().filter(r -> (r.getCategoryId() == cateId && r.getStreet().getDistrict().getId() == disId)).skip(limitStart).limit(limitEnd).collect(Collectors.toList());
//                } else {
//                    foodFielters = foods.stream().filter(r -> (r.getCategoryId() == cateId && r.getStreet().getDistrict().getId() == disId && r.getStreet().getId() == streetId)).skip(limitStart).limit(limitEnd).collect(Collectors.toList());
//                }
//            }
//        }

        return null;
    }
}
