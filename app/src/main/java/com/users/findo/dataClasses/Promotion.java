package com.users.findo.dataClasses;

import com.google.firebase.Timestamp;

public class Promotion {
    String promoId;
    double amount;
    String imageUrl,orderId,storeId;
    boolean paymentStatus;
    Timestamp startDate, endDate;

    public Promotion(){

    }

    public Promotion(String promoId, double amount, String imageUrl, String orderId, String storeId, boolean paymentStatus, Timestamp startDate, Timestamp endDate) {
        this.promoId = promoId;
        this.amount = amount;
        this.imageUrl = imageUrl;
        this.orderId = orderId;
        this.storeId = storeId;
        this.paymentStatus = paymentStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
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
