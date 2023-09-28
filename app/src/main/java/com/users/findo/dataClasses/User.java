package com.users.findo.dataClasses;

import java.util.List;

public class User {

    String name,email,image, referralCode;
    List<String> cart;

    public User(String name, String email, String image, String referalCode, List<String> cart) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.referralCode = referalCode;
        this.cart = cart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public List<String> getCart() {
        return cart;
    }

    public void setCart(List<String> cart) {
        this.cart = cart;
    }
}
