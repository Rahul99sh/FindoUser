package com.users.findo.dataClasses;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Store implements Parcelable {
    List<String> promoIds;
    private String StoreName;
    private Double StoreLat;
    private float dist;
    private Double rating;
    private int visits;
    private boolean verified;
    private Double StoreLong;
    private String StoreUrl;
    private String OwnerId,address,gstin,licenceNo;
    private String StoreId;
    public Store() {}

    public Store(List<String> promoIds, String storeName, Double storeLat, Double rating, int visits, boolean verified, Double storeLong, String storeUrl, String ownerId, String address, String gstin, String licenceNo, String storeId, float dist) {
        this.promoIds = promoIds;
        StoreName = storeName;
        StoreLat = storeLat;
        this.rating = rating;
        this.visits = visits;
        this.verified = verified;
        StoreLong = storeLong;
        StoreUrl = storeUrl;
        OwnerId = ownerId;
        this.address = address;
        this.gstin = gstin;
        this.licenceNo = licenceNo;
        StoreId = storeId;
        this.dist = dist;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public List<String> getPromoIds() {
        return promoIds;
    }

    public void setPromoIds(List<String> promoIds) {
        this.promoIds = promoIds;
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

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    protected Store(Parcel in) {
        promoIds = in.createStringArrayList();
        StoreName = in.readString();
        if (in.readByte() == 0) {
            StoreLat = null;
        } else {
            StoreLat = in.readDouble();
        }
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        visits = in.readInt();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            verified = in.readBoolean();
        }
        if (in.readByte() == 0) {
            StoreLong = null;
        } else {
            StoreLong = in.readDouble();
        }
        StoreUrl = in.readString();
        OwnerId = in.readString();
        address = in.readString();
        gstin = in.readString();
        licenceNo = in.readString();
        StoreId = in.readString();
        dist = in.readFloat();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(promoIds);
        dest.writeString(StoreName);
        if (StoreLat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(StoreLat);
        }
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
        dest.writeInt(visits);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(verified);
        }
        if (StoreLong == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(StoreLong);
        }
        dest.writeString(StoreUrl);
        dest.writeString(OwnerId);
        dest.writeString(address);
        dest.writeString(gstin);
        dest.writeString(licenceNo);
        dest.writeString(StoreId);
        dest.writeFloat(dist);
    }
}


