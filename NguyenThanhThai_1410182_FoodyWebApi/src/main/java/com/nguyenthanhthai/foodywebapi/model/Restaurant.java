package com.nguyenthanhthai.foodywebapi.model;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

/**
 * Created by NguyenThanhThai on 4/27/2017.
 */
@Entity
public class Restaurant {
    @Id
    Long restaurantId;
    @Column(columnDefinition = "NVARCHAR(255)")
    String name, nameEn;
    @Column(columnDefinition = "NVARCHAR(255)")
    String address;
    /*
    * provinceId
    * AreasViewMenuId =district gan
    * */
    @ManyToOne(cascade = {CascadeType.ALL, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER, optional = false)
    Street street;

    //Vi do kinh do
    Double latitude;
    Double longtitude;;
    String phone;
    @Column(columnDefinition = "NVARCHAR(255)")
    String mobileImageUrl;
    @Column(columnDefinition = "NVARCHAR(255)")
    String photoUrl;
    Long totalReviews;
    Long totalPictures;
    Long totalViews;
    Double avgRating;
    Long featurePicture;
    //TODO TopReViews
    @OneToMany(cascade = {CascadeType.ALL, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Feedback> feedbacks;

    @Column(columnDefinition = "NVARCHAR(255)")
    String locationUrlRewriteName;
    //MethodOrder 2 3 5 7 Services
    Long methodOrder;

    //ForeignKey

    /*
    * categoryTypeId=categoryGroupId
    * categoryId=CuisineViewMenuId
    * */
    Long categoryTypeId, categoryId;

    @Column(columnDefinition = "NVARCHAR(255)")
    String videoName;

    public Restaurant() {
    }

    public Restaurant(Long restaurantId, String name, String nameEn, String address, Double latitude, Double longtitude, String phone, String mobileImageUrl, String photoUrl, Long totalReviews, Long totalPictures, Long totalViews, Double avgRating, String locationUrlRewriteName) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.nameEn = nameEn;
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.phone = phone;
        this.mobileImageUrl = mobileImageUrl;
        this.photoUrl = photoUrl;
        this.totalReviews = totalReviews;
        this.totalPictures = totalPictures;
        this.totalViews = totalViews;
        this.avgRating = avgRating;
        this.locationUrlRewriteName = locationUrlRewriteName;

        methodOrderRamdom();
    }

    public Restaurant(Long restaurantId, String name, String nameEn, String address, Street street, Double latitude, Double longtitude, String phone, String mobileImageUrl, String photoUrl, Long totalReviews, Long totalPictures, Long totalViews, Double avgRating, Long featurePicture, List<Feedback> feedbacks, String locationUrlRewriteName, Long methodOrder, Long categoryTypeId, Long categoryId, String videoName) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.nameEn = nameEn;
        this.address = address;
        this.street = street;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.phone = phone;
        this.mobileImageUrl = mobileImageUrl;
        this.photoUrl = photoUrl;
        this.totalReviews = totalReviews;
        this.totalPictures = totalPictures;
        this.totalViews = totalViews;
        this.avgRating = avgRating;
        this.featurePicture = featurePicture;
        this.feedbacks = feedbacks;
        this.locationUrlRewriteName = locationUrlRewriteName;
        this.methodOrder = methodOrder;
        this.categoryTypeId = categoryTypeId;
        this.categoryId = categoryId;
        this.videoName = videoName;
    }

    /*
        * Random method order number
        * */
    private void methodOrderRamdom() {
        Random random = new Random();

        int ram = (random.nextInt(1001)) % 4;
        switch (ram) {
            case 0:
                this.setMethodOrder(1L);
                break;
            case 1:
                this.setMethodOrder(2L);
                if (((random.nextInt(1001)) % 4) % 2 == 1) {
                    switch ((random.nextInt(1001)) % 4) {
                        case 2:
                            this.setMethodOrder(this.getMethodOrder() * 3);
                            break;
                        case 3:
                            this.setMethodOrder(this.getMethodOrder() * 5);
                            break;
                        case 4:
                            this.setMethodOrder(this.getMethodOrder() * 7);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case 2:
                this.setMethodOrder(3L);
                if (((random.nextInt(1001)) % 4) % 2 == 1) {
                    switch ((random.nextInt(1001)) % 4) {
                        case 1:
                            this.setMethodOrder(this.getMethodOrder() * 2);
                            break;
                        case 3:
                            this.setMethodOrder(this.getMethodOrder() * 5);
                            break;
                        case 4:
                            this.setMethodOrder(this.getMethodOrder() * 7);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case 3:
                this.setMethodOrder(5L);
                if (((random.nextInt(1001)) % 4) % 2 == 1) {
                    switch ((random.nextInt(1001)) % 4) {
                        case 1:
                            this.setMethodOrder(this.getMethodOrder() * 2);
                            break;
                        case 3:
                            this.setMethodOrder(this.getMethodOrder() * 3);
                            break;
                        case 4:
                            this.setMethodOrder(this.getMethodOrder() * 7);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case 4:
                this.setMethodOrder(7L);
                if (((random.nextInt(1001)) % 4) % 2 == 1) {
                    switch ((random.nextInt(1001)) % 4) {
                        case 1:
                            this.setMethodOrder(this.getMethodOrder() * 2);
                            break;
                        case 3:
                            this.setMethodOrder(this.getMethodOrder() * 3);
                            break;
                        case 4:
                            this.setMethodOrder(this.getMethodOrder() * 5);
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                this.setMethodOrder(this.getMethodOrder() * 1);
        }
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }


//    public List<Feedback> getFeedbacks() {
//        return Feedback.find(Feedback.class, "RESTAURANT_ID = ?", this.restaurantid.toString());
//    }
//
//    /*
//    * get Feedback for what fragment
//    * */
//    public Feedback getFirstFeedbacks() {
//        return Select.from(Feedback.class).where(Condition.prop("RESTAURANT_ID").eq(this.getId().toString())).first();
//    }
//
//    /*
//    * get Feedback for where fragment
//    * */
//    public List<Feedback> getTwoFeedbacks() {
//        return Select.from(Feedback.class).where(Condition.prop("RESTAURANT_ID").eq(this.getId().toString())).limit("2").list();
//    }
//
//    public void setFeedbacks(List<Feedback> feedbacks) {
//        this.feedbacks = feedbacks;
//    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobileImageUrl() {
        return mobileImageUrl;
    }

    public void setMobileImageUrl(String mobileImageUrl) {
        this.mobileImageUrl = mobileImageUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public Long getTotalPictures() {
        return totalPictures;
    }

    public void setTotalPictures(Long totalPictures) {
        this.totalPictures = totalPictures;
    }

    public Long getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Long totalViews) {
        this.totalViews = totalViews;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Long getFeaturePicture() {
        return featurePicture;
    }

    public void setFeaturePicture(Long featurePicture) {
        this.featurePicture = featurePicture;
    }

    public String getLocationUrlRewriteName() {
        return locationUrlRewriteName;
    }

    public void setLocationUrlRewriteName(String locationUrlRewriteName) {
        this.locationUrlRewriteName = locationUrlRewriteName;
    }

    public Long getMethodOrder() {
        return methodOrder;
    }

    public void setMethodOrder(Long methodOrder) {
        this.methodOrder = methodOrder;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public Long getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(Long categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}