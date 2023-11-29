package com.users.findo.dataClasses;

public class CartList {

    String cartListText;
    String ImageUrl;

    public CartList(){

    }


    public CartList(String cartListText, String imageUrl) {
        this.cartListText = cartListText;
        ImageUrl = imageUrl;
    }

    public String getCartListText() {
        return cartListText;
    }

    public void setCartListText(String cartListText) {
        this.cartListText = cartListText;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
