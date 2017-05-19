package com.example.nguyenthanhthai.foody.modelnew;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.nguyenthanhthai.foody.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenThanhThai on 4/27/2017.
 */
public class Restaurant {
    Long restaurantId;
    String name, nameEn;
    String address;
    //Vi do kinh do
    Long latitude;
    Long longtitude;
    String phone;
    String mobileImageUrl;
    String photoUrl;
    Long totalReviews;
    Long totalPictures;
    Long totalViews;
    Double avgRating;
    Long featurePicture;
    //TODO TopReViews
    List<Feedback> feedbacks;

    String locationUrlRewriteName;
    //MethodOrder 2 3 5 7 Services
    Long methodOrder;

    //ForeignKey
    Long districtId;
    Integer categorytypeid, streetid, categoryid;
    String videoName;

    public Restaurant() {
    }

    public Restaurant(Long restaurantId, String name, String nameEn, String address, Long latitude, Long longtitude, String phone, String mobileImageUrl, String photoUrl, Long totalReviews, Long totalPictures, Long totalViews, Double avgRating, String locationUrlRewriteName, Long methodOrder) {
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
        this.methodOrder = methodOrder;
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

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Long longtitude) {
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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Integer getCategorytypeid() {
        return categorytypeid;
    }

    public void setCategorytypeid(Integer categorytypeid) {
        this.categorytypeid = categorytypeid;
    }

    public Integer getStreetid() {
        return streetid;
    }

    public void setStreetid(Integer streetid) {
        this.streetid = streetid;
    }

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }


    /**
     * Select item with categoryType, with limit 3
     */
    public interface VolleyCallback {
        void onSuccess(List<Restaurant> restaurants);
    }


    public static boolean selectWithCategoryTypeLimit(Context context, Long catagoryTypeId, Long categoryId, Long districtId, Long streetId, Long limitStart, Long limitEnd, final VolleyCallback callback) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = context.getResources().getString(R.string.serrver_api)
                + "restaurant/fielter/" + catagoryTypeId + "/" + categoryId + "/" + 217 + "/" + districtId + "/" + streetId + "/" + limitStart + "/" + limitEnd + "/";

        Log.d("VolleyError", url);
        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final List<Restaurant> restaurants = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        restaurants.add(paserJsonRestaurant(response.getJSONObject(i)));
                        Log.d("VolleyError", response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.onSuccess(restaurants);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", error.toString());
                Log.d("VolleyError", url);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return true;
    }

    private static Restaurant paserJsonRestaurant(JSONObject jsonRestaurnt) {
        Restaurant restaurant = null;
        try {
            Long restaurantId = jsonRestaurnt.getLong("restaurantId");
            String name = jsonRestaurnt.getString("name");
            String nameEn = jsonRestaurnt.getString("nameEn");
            String address = jsonRestaurnt.getString("address");
            Long latitude = jsonRestaurnt.getLong("latitude");
            Long longtitude = jsonRestaurnt.getLong("longtitude");
            String phone = jsonRestaurnt.getString("phone");
            String mobileImageUrl = jsonRestaurnt.getString("mobileImageUrl");
            String photoUrl = jsonRestaurnt.getString("photoUrl");
            Long totalReviews = jsonRestaurnt.getLong("totalReviews");
            Long totalPictures = jsonRestaurnt.getLong("totalPictures");
            Long totalViews = jsonRestaurnt.getLong("totalViews");
            Double avgRating = jsonRestaurnt.getDouble("avgRating");
            String locationUrlRewriteName = jsonRestaurnt.getString("locationUrlRewriteName");
            Long methodOrder = jsonRestaurnt.getLong("methodOrder");
            restaurant = new Restaurant(restaurantId, name, nameEn, address, latitude, longtitude,
                    phone, mobileImageUrl, photoUrl, totalReviews, totalPictures, totalViews, avgRating,
                    locationUrlRewriteName, methodOrder);
            List<Feedback> feedbacks = new ArrayList<>();
            JSONArray jsonFeedbacks = jsonRestaurnt.getJSONArray("feedbacks");
            for (int i = 0; i < jsonFeedbacks.length(); i++) {
                feedbacks.add(paserJsonFeedback(jsonFeedbacks.getJSONObject(i)));
            }
            restaurant.setFeedbacks(feedbacks);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurant;
    }

    private static Feedback paserJsonFeedback(JSONObject jsonFeedback) {
        Feedback feedback = null;
        try {
            Long reviewId = jsonFeedback.getLong("reviewId");
            Long restaurantId = jsonFeedback.getLong("restaurantId");
            String comment = jsonFeedback.getString("comment");
            String title = jsonFeedback.getString("title");
            Double avgRating = jsonFeedback.getDouble("avgRating");

            feedback = new Feedback(reviewId, restaurantId, comment, title, avgRating);
            JSONObject jsonUser = jsonFeedback.getJSONObject("user");
            User user = new User(
                    jsonUser.getLong("userId"),
                    jsonUser.getString("userName"),
                    jsonUser.getString("email"),
                    jsonUser.getString("fullName"),
                    jsonUser.getString("ownerFirstName"),
                    jsonUser.getString("ownerLastName"),
                    jsonUser.getString("avatarURL"),
                    jsonUser.getBoolean("gender")
            );
            feedback.setUser(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return feedback;
    }


    public static boolean selectRetaurntNear(Context context, Double latitude, Double longtitude, Long limitStart, Long limitEnd, final VolleyCallback callback) {

        final List<Restaurant> restaurants = new ArrayList<>();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = context.getResources().getString(R.string.serrver_api)
                + "restaurant/near/" + latitude + "/" + longtitude + "/" + limitStart + "/" + limitEnd + "/";

        Log.d("VolleyError", url);
        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final List<Restaurant> restaurants = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        restaurants.add(paserJsonRestaurant(response.getJSONObject(i)));
                        Log.d("VolleyError", response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.onSuccess(restaurants);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", error.toString());
                Log.d("VolleyError", url);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return true;
    }
}