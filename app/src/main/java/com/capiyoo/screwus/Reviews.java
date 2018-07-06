package com.capiyoo.screwus;

public class Reviews {


    private String mFrom;
    private String mReview;
    private float mRating;
    private String mArea;

    public Reviews() {
    }

    public String getmArea() {
        return mArea;
    }

    public void setmArea(String mArea) {
        this.mArea = mArea;
    }

    public Reviews(String mFrom, String mReview, float mRating) {
        this.mFrom = mFrom;
        this.mReview = mReview;
        this.mRating = mRating;
    }

    public String getmFrom() {
        return mFrom;

    }

    public void setmFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String mReview) {
        this.mReview = mReview;
    }

    public float getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }
}
