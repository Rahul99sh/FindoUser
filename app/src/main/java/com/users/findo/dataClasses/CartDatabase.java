package com.users.findo.dataClasses;

public class CartDatabase {
    String StoreId;
    String ItemId;
    String ItemName;
    String StoreName;
    String StoreUrl;
    String ItemUrl;
    double StoreLat;
    double StoreLong;

    String ItemCategory;

    String ItemTag;

    String Price;

    public CartDatabase(){

    }

    public CartDatabase( String storeId, String itemId, String itemName, String storeName, String storeUrl, String itemUrl, double storeLat, double storeLong, String itemCategory,String itemTag,String price) {
        StoreId = storeId;
        ItemId = itemId;
        ItemName = itemName;
        StoreName = storeName;
        StoreUrl = storeUrl;
        ItemUrl = itemUrl;
        StoreLat = storeLat;
        StoreLong = storeLong;
        ItemCategory = itemCategory;
        ItemTag = itemTag;
        Price = price;
    }

    public String getItemTag() {
        return ItemTag;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setItemTag(String itemTag) {
        ItemTag = itemTag;
    }

    public String getItemCategory() {
        return ItemCategory;
    }

    public void setItemCategory(String itemCategory) {
        ItemCategory = itemCategory;
    }

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getStoreUrl() {
        return StoreUrl;
    }

    public void setStoreUrl(String storeUrl) {
        StoreUrl = storeUrl;
    }

    public String getItemUrl() {
        return ItemUrl;
    }

    public void setItemUrl(String itemUrl) {
        ItemUrl = itemUrl;
    }

    public double getStoreLat() {
        return StoreLat;
    }

    public void setStoreLat(double storeLat) {
        StoreLat = storeLat;
    }

    public double getStoreLong() {
        return StoreLong;
    }

    public void setStoreLong(double storeLong) {
        StoreLong = storeLong;
    }
}
