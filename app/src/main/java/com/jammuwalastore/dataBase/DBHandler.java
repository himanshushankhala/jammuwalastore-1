package com.jammuwalastore.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jammuwalastore.model.ProductModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kishan Singh .
 */
public class DBHandler extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "ProductCart";
    // Cart table name
    public static final String TABLE_PRODUCT_CART = "Cart";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;


    // Cart Table Columns names
    private static final String KEY_P_ID = "P_Id";
    private static final String KEY_PRODUCT_NAME = "ProductName";
    private static final String KEY_PRODUCT_PRICE = "ProductPrice";
    private static final String KEY_PRODUCT_QUANTITY = "ProductQuantiy";
    private static final String KEY_PRODUCT_ID = "ProductID";
    private static final String KEY_PRODUCT_IMAGE = "ProductImage";
    private static final String KEY_TOTAL_PRICE = "TotalPrice";
    private static final String KEY_WEIGHT = "ProductWeight";
    private static final String KEY_TAX_STATUS = "TaxStatus";
    private static final String KEY_TAX_CLASS = "TaxClass";


    private String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_PRODUCT_CART + "("
            + KEY_P_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRODUCT_PRICE + " TEXT," + KEY_PRODUCT_QUANTITY + " INTEGER," + KEY_PRODUCT_ID + " TEXT,"
            + KEY_PRODUCT_IMAGE + " TEXT," + KEY_TOTAL_PRICE + " TEXT," + KEY_WEIGHT + " TEXT,"
            + KEY_TAX_STATUS + " TEXT," + KEY_TAX_CLASS + " TEXT" + ")";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL("delete from"+ TABLE_CATEGORY_NAME);
        db.execSQL(CREATE_CART_TABLE);
        //  db.execSQL(CREATE_ADDRESS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_CART);

        // Create tables again
        onCreate(db);
    }

    // Category Table Methods
    public void addProduct(ProductModel product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, category.getID());
        values.put(KEY_PRODUCT_NAME, product.getProductName()); // Contact Name
        values.put(KEY_PRODUCT_PRICE, product.getProductPrice()); // Contact Phone
        values.put(KEY_PRODUCT_QUANTITY, product.getProductQuantity());
        values.put(KEY_PRODUCT_ID, product.getProductid()); // Contact Phone
        values.put(KEY_PRODUCT_IMAGE, product.getProductImage());
        values.put(KEY_TOTAL_PRICE, product.getTotalPrice());
        values.put(KEY_WEIGHT, product.getProduct_weight());
        values.put(KEY_TAX_STATUS, product.getTax_status());
        values.put(KEY_TAX_CLASS, product.getTax_class());

        // Inserting Row
        db.insert(TABLE_PRODUCT_CART, null, values);
        db.close(); // Closing database connection
    }


/*    // Getting single contact
    public ProductModel SingleProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCT_CART, new String[]{KEY_P_ID,
                        KEY_PRODUCT_NAME, KEY_PRODUCT_PRICE, KEY_PRODUCT_QUANTITY, KEY_PRODUCT_ID, KEY_SKU, KEY_TOTAL_PRICE}, KEY_SKU + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ProductModel productModel = new ProductModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        // return contact
        return productModel;
    }*/

    // Getting All Contacts
    public List<ProductModel> getAllProducts() {
        List<ProductModel> productList = new ArrayList<ProductModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_CART;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProductModel productModel = new ProductModel();
                productModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_P_ID))));
                productModel.setProductName(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_NAME)));
                productModel.setProductPrice(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_PRICE)));
                productModel.setProductQuantity(cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_QUANTITY)));
                productModel.setProductid(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_ID)));
                productModel.setTotalPrice(cursor.getString(cursor.getColumnIndex(KEY_TOTAL_PRICE)));
                productModel.setProductImage(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_IMAGE)));
                productModel.setProduct_weight(cursor.getString(cursor.getColumnIndex(KEY_WEIGHT)));
                productModel.setTax_status(cursor.getString(cursor.getColumnIndex(KEY_TAX_STATUS)));
                productModel.setTax_class(cursor.getString(cursor.getColumnIndex(KEY_TAX_CLASS)));
                // Adding Value to list
                productList.add(productModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return productList;
    }


    // Updating single contact
    public int updateProducts(ProductModel product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, product.getProductName()); // Contact Name
        values.put(KEY_PRODUCT_PRICE, product.getProductPrice()); // Contact Phone
        values.put(KEY_PRODUCT_QUANTITY, product.getProductQuantity());
        values.put(KEY_PRODUCT_ID, product.getProductid()); // Contact Phone
        values.put(KEY_PRODUCT_IMAGE, product.getProductImage());
        values.put(KEY_TOTAL_PRICE, product.getTotalPrice());
        values.put(KEY_WEIGHT, product.getProduct_weight());
        // updating row
        return db.update(TABLE_PRODUCT_CART, values, KEY_PRODUCT_ID + " = ?",
                new String[]{(product.getProductid())});
    }

    // Deleting single contact
    public void deleteProduct(String productId, String weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT_CART, KEY_PRODUCT_ID + " = ?",
                new String[]{productId});
        db.close();
    }


   /* // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCT_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }*/

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCT_CART);
        db.close();
    }



    /*// Getting single Add
    Address getAddress(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ADDRESS_DETAIL, new String[]{KEY_ADDRESS_ID,
                        KEY_NAME, KEY_LNAME, KEY_MOBILE, KEY_EMAIL, KEY_ADDRESS1, KEY_ADDRESS2}, KEY_ADDRESS_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Address address = new Address(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
        // return contact
        return address;
    }*/

    // Getting All Advertisement
   /* public List<AddressFragment> getAllAddess() {
        List<AddressFragment> addresses = new ArrayList<AddressFragment>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS_DETAIL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddressFragment address = new AddressFragment();
                address.setAddressid(Integer.parseInt(cursor.getString(0)));
                address.setName(cursor.getString(1));
                address.setLName(cursor.getString(2));
                address.setMobile(cursor.getString(3));
                address.setEmail(cursor.getString(4));
                address.setAddress1(cursor.getString(5));
                address.setAddress2(cursor.getString(6));
                address.setPinCode(cursor.getString(7));
                address.setArea(cursor.getString(8));
                // Adding advertisements to list
                addresses.add(address);
            } while (cursor.moveToNext());
        }

        // return advertisements list
        return addresses;
    }



    // Deleting single contact
    public void deleteAddress(AddressFragment address) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADDRESS_DETAIL, KEY_ADDRESS_ID + " = ?",
                new String[]{String.valueOf(address.getAddressid())});
        db.close();
    }*/


}




