package com.users.findo.dataClasses;

public class Items {

    String ImageUrl;
    String ItemName;


    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public Items(String ImageUrl, String ItemName) {
        this.ImageUrl = ImageUrl;
        this.ItemName = ItemName;
    }
}
