package com.users.findo.dataClasses;

import android.os.Parcel;
import android.os.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String ItemUrl;
    private Double StoreLat;
    private Double StoreLong;
    private String StoreUrl;
    private String StoreName;
    private String ItemName, ItemDescription, price, ItemId, Category, storeID, ItemTag;
    private double ItemRating;
    private int clicks, addedToCart;
    private boolean arEnabled;
    private String arModelLink;

    public Item() {
    }

    public Item(String itemUrl, Double storeLat, Double storeLong, String storeUrl, String storeName, String itemName, String itemDescription,
                String price, String itemId,
                String category, String storeID, String itemTag, double itemRating, int clicks, int addedToCart, boolean areEnabled, String arModelLink) {
        ItemUrl = itemUrl;
        StoreLat = storeLat;
        StoreLong = storeLong;
        StoreUrl = storeUrl;
        StoreName = storeName;
        ItemName = itemName;
        ItemDescription = itemDescription;
        this.price = price;
        ItemId = itemId;
        Category = category;
        this.storeID = storeID;
        ItemTag = itemTag;
        ItemRating = itemRating;
        this.clicks = clicks;
        this.addedToCart = addedToCart;
        this.arEnabled = areEnabled;
        this.arModelLink = arModelLink;
    }

    protected Item(Parcel in) {
        ItemUrl = in.readString();
        StoreLat = in.readDouble();
        StoreLong = in.readDouble();
        StoreUrl = in.readString();
        StoreName = in.readString();
        ItemName = in.readString();
        ItemDescription = in.readString();
        price = in.readString();
        ItemId = in.readString();
        Category = in.readString();
        ItemTag = in.readString();
        ItemRating = in.readDouble();
        clicks = in.readInt();
        addedToCart = in.readInt();
        storeID = in.readString();
        arEnabled = in.readByte() != 0;
        arModelLink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ItemUrl);
        dest.writeDouble(StoreLat);
        dest.writeDouble(StoreLong);
        dest.writeString(StoreUrl);
        dest.writeString(StoreName);
        dest.writeString(ItemName);
        dest.writeString(ItemDescription);
        dest.writeString(price);
        dest.writeString(ItemId);
        dest.writeString(Category);
        dest.writeString(ItemTag);
        dest.writeDouble(ItemRating);
        dest.writeInt(clicks);
        dest.writeInt(addedToCart);
        dest.writeString(storeID);
        dest.writeByte((byte) (arEnabled ? 1 : 0));
        dest.writeString(arModelLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isArEnabled() {
        return arEnabled;
    }

    public void setArEnabled(boolean arEnabled) {
        this.arEnabled = arEnabled;
    }

    public String getArModelLink() {
        return arModelLink;
    }

    public void setArModelLink(String arModelLink) {
        this.arModelLink = arModelLink;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getItemTag() {
        return ItemTag;
    }

    public void setItemTag(String itemTag) {
        ItemTag = itemTag;
    }

    public double getItemRating() {
        return ItemRating;
    }

    public void setItemRating(double itemRating) {
        ItemRating = itemRating;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getAddedToCart() {
        return addedToCart;
    }

    public void setAddedToCart(int addedToCart) {
        this.addedToCart = addedToCart;
    }

    public String getItemUrl() {
        return ItemUrl;
    }

    public void setItemUrl(String itemUrl) {
        ItemUrl = itemUrl;
    }
}
