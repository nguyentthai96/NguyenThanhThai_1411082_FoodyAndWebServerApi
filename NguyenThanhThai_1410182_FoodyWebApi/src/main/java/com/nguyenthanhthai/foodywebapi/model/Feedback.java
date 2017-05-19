package com.nguyenthanhthai.foodywebapi.model;

import javax.persistence.*;

/**
 * Created by NguyenThanhThai on 4/27/2017.
 */
@Entity
public class Feedback {
    @Id
    Long reviewId;
    Long restaurantId;

    @Lob
    @Column(columnDefinition = "LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin")
    String comment;
    @Lob
    @Column(columnDefinition = "LONGTEXT CHARACTER SET 'utf8'")
    String title;
    Double avgRating;
    @Column(columnDefinition = "NVARCHAR(255)")
    String createdOn;
    @Column(columnDefinition = "NVARCHAR(255)")
    String createdOnTimeDiff;

    @ManyToOne(cascade = {CascadeType.ALL, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER, optional = false)
    User user;

    public Feedback() {
    }

    public Feedback(Long reviewId, Long restaurantId, String comment, String title, Double avgRating) {
        this.reviewId = reviewId;
        this.restaurantId = restaurantId;
        this.comment = comment;
        this.title = title;
        this.avgRating = avgRating;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedOnTimeDiff() {
        return createdOnTimeDiff;
    }

    public void setCreatedOnTimeDiff(String createdOnTimeDiff) {
        this.createdOnTimeDiff = createdOnTimeDiff;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
}
