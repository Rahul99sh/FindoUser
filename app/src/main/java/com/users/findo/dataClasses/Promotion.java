package com.users.findo.dataClasses;

public class Promotion {
    String promoId;
    String imageUrl;

    public Promotion(){

    }

    public Promotion(String promoId, String imageUrl) {
        this.promoId = promoId;
        this.imageUrl = imageUrl;
    }

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
