package com.users.findo.dataClasses;

public class Category {
    String categoryName;
    int backColor;
    int strokeColor;
    int imageId;

    public Category(){

    }

    public Category(String categoryName, int imageId,int backColor,int strokeColor) {
        this.categoryName = categoryName;
        this.imageId = imageId;
        this.backColor = backColor;
        this.strokeColor = strokeColor;
    }

    public int getBackColor() {
        return backColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
