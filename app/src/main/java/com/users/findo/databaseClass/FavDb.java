package com.users.findo.databaseClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.users.findo.dataClasses.CartDatabase;
import com.users.findo.dataClasses.Item;

import java.util.ArrayList;

public class FavDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FindoUsersFav.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Fav";
    private static final String ITEM_CATEGORY = "Category";
    private static final String STORE_ID = "storeID";
    private static final String STORE_NAME = "storeName";
    private static final String STORE_IMAGE = "storeImage";

    private static final String STORE_LAT = "storeLat";
    private static final String STORE_LONG = "storeLong";

    private static final String ITEM_ID = "itemID";
    private static final String ITEM_IMAGE = "itemImage";
    private static final String ITEM_NAME = "itemName";
    private static final String ITEM_TAG = "itemTag";

    private static final String ITEM_PRICE = "itemPrice";

    private static final String ITEM_DESC = "itemDesc";
    private static final String ITEM_Rating = "itemRating";
    private static final String ITEM_CLICK = "itemClick";
    private static final String ITEM_ADD = "itemADD";
    private static final String ITEM_AR = "itemAr";
    private static final String ITEM_AR_LINK = "itemArLink";
    public SQLiteDatabase getDatabase() {
        return getWritableDatabase();
    }

    public FavDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                STORE_ID + " TEXT, " +
                STORE_NAME + " TEXT, " +
                STORE_IMAGE + " TEXT, " +
                STORE_LAT + " TEXT, " +
                STORE_LONG + " TEXT, " +
                ITEM_ID + " TEXT PRIMARY KEY, " +
                ITEM_IMAGE + " TEXT, " +
                ITEM_NAME + " TEXT, " +
                ITEM_CATEGORY + " TEXT, " +
                ITEM_TAG + " TEXT, " +
                ITEM_PRICE + " TEXT, " +
                ITEM_DESC + " TEXT, " +  // Add the missing column here
                ITEM_Rating + " TEXT, " +
                ITEM_AR + " TEXT, " +
                ITEM_AR_LINK + " TEXT, " +
                ITEM_CLICK + " TEXT, " +
                ITEM_ADD + " TEXT)");    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(ArrayList<Item> data) {
        SQLiteDatabase db = getWritableDatabase();
        for (int i = 0; i < data.size(); i++) {
            Item cart = data.get(i);
            ContentValues values = new ContentValues();
            values.put(STORE_ID, cart.getStoreID());
            values.put(STORE_NAME, cart.getStoreName());
            values.put(STORE_IMAGE, cart.getStoreUrl());
            values.put(STORE_LAT, cart.getStoreLat());
            values.put(STORE_LONG, cart.getStoreLong());
            values.put(ITEM_ID, cart.getItemId());
            values.put(ITEM_IMAGE, cart.getItemUrl());
            values.put(ITEM_NAME, cart.getItemName());
            values.put(ITEM_CATEGORY, cart.getCategory());
            values.put(ITEM_TAG, cart.getItemTag());
            values.put(ITEM_PRICE, cart.getPrice());
            db.insert(TABLE_NAME, null, values);
        }
        db.close();
    }

    public void insertOneItem( Item cart){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STORE_ID, cart.getStoreID());
        values.put(STORE_NAME, cart.getStoreName());
        values.put(STORE_IMAGE, cart.getStoreUrl());
        values.put(STORE_LAT, cart.getStoreLat());
        values.put(STORE_LONG, cart.getStoreLong());
        values.put(ITEM_ID, cart.getItemId());
        values.put(ITEM_IMAGE, cart.getItemUrl());
        values.put(ITEM_NAME, cart.getItemName());
        values.put(ITEM_CATEGORY, cart.getCategory());
        values.put(ITEM_TAG, cart.getItemTag());
        values.put(ITEM_PRICE, cart.getPrice());
        values.put(ITEM_DESC, cart.getItemDescription());
        values.put(ITEM_Rating, cart.getItemRating());
        values.put(ITEM_CLICK, cart.getClicks());
        values.put(ITEM_ADD, cart.getAddedToCart());
        values.put(ITEM_AR, cart.isArEnabled());
        values.put(ITEM_AR_LINK, cart.getArModelLink());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public boolean isTableEmpty(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        boolean isEmpty = cursor.getCount() == 0;
        cursor.close();
        return isEmpty;
    }

    public ArrayList<Item> getData() {
        ArrayList<Item> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                ///////////////////////////////////////////////////////////
                String store_id = cursor.getString(cursor.getColumnIndex(STORE_ID));
                String store_name = cursor.getString(cursor.getColumnIndex(STORE_NAME));
                String store_image = cursor.getString(cursor.getColumnIndex(STORE_IMAGE));
                String store_lat = cursor.getString(cursor.getColumnIndex(STORE_LAT));
                String store_long = cursor.getString(cursor.getColumnIndex(STORE_LONG));
                String item_id = cursor.getString(cursor.getColumnIndex(ITEM_ID));
                String item_image = cursor.getString(cursor.getColumnIndex(ITEM_IMAGE));
                String item_name = cursor.getString(cursor.getColumnIndex(ITEM_NAME));
                String item_category = cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY));
                String item_tag = cursor.getString(cursor.getColumnIndex(ITEM_TAG));
                String item_price = cursor.getString(cursor.getColumnIndex(ITEM_PRICE));
                String item_add = cursor.getString(cursor.getColumnIndex(ITEM_ADD));
                String item_click = cursor.getString(cursor.getColumnIndex(ITEM_CLICK));
                String item_rating = cursor.getString(cursor.getColumnIndex(ITEM_Rating));
                String item_ar = cursor.getString(cursor.getColumnIndex(ITEM_AR));
                String item_ar_link = cursor.getString(cursor.getColumnIndex(ITEM_AR_LINK));
                data.add(new Item( item_image, Double.parseDouble(store_lat), Double.parseDouble(store_long) ,store_image,store_name, item_name, "", item_price, item_id,
                        item_category,store_id,item_tag,Double.parseDouble(item_rating),Integer.parseInt(item_click), Integer.parseInt(item_add), Boolean.parseBoolean(item_ar), item_ar_link));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    public boolean updateData(Item cart) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STORE_ID, cart.getStoreID());
        values.put(STORE_NAME, cart.getStoreName());
        values.put(STORE_IMAGE, cart.getStoreUrl());
        values.put(STORE_LAT, cart.getStoreLat());
        values.put(STORE_LONG, cart.getStoreLong());
        values.put(ITEM_ID, cart.getItemId());
        values.put(ITEM_IMAGE, cart.getItemUrl());
        values.put(ITEM_NAME, cart.getItemName());
        values.put(ITEM_CATEGORY, cart.getCategory());
        values.put(ITEM_TAG, cart.getItemTag());
        values.put(ITEM_PRICE, cart.getPrice());

        int result = db.update(TABLE_NAME, values, ITEM_ID + " = ?",new String[] {cart.getItemId()});
        db.close();
        return result > 0;
    }

    public void deleteItem(String itemId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, ITEM_ID + " = ?",new String[] {itemId});
        db.close();
    }
    public boolean itemExist(String itemId) {
        SQLiteDatabase db = getWritableDatabase();

        boolean exists= false;
        if(itemId != null) {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_ID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{itemId});

            exists = cursor.getCount() > 0;
            cursor.close();
        }
        db.close();

        return exists;
    }

}

