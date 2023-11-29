package com.users.findo.dataClasses;

public class Store {
    String StoreName;
    Double StoreLat;
    Double StoreLong;
    String StoreUrl;

    String Distance;

    float dist;

    String StoreId;

    public Store() {

    }

    public Store(String storeName, Double storeLat, Double storeLong, String storeUrl, String distance, float dist, String storeId) {
        StoreName = storeName;
        StoreLat = storeLat;
        StoreLong = storeLong;
        StoreUrl = storeUrl;
        Distance = distance;
        this.dist = dist;
        StoreId = storeId;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public Double getStoreLat() {
        return StoreLat;
    }

    public void setStoreLat(Double storeLat) {
        StoreLat = storeLat;
    }

    public Double getStoreLong() {
        return StoreLong;
    }

    public void setStoreLong(Double storeLong) {
        StoreLong = storeLong;
    }

    public String getStoreUrl() {
        return StoreUrl;
    }

    public void setStoreUrl(String storeUrl) {
        StoreUrl = storeUrl;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }
}

