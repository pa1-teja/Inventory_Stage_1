package com.example.pavan.inventoryapp;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pavan.inventoryapp.DataStore.InventoryContract;

public class MainActivity extends AppCompatActivity{


    String[] projection = {
            InventoryContract.InventoryEntry._ID,
            InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,
            InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_PRICE,
            InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_QUANTITY,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE
    };


    private String productName = "Mac";
    private long productPrice = 1000;
    private long quantity = 10;
    private String supplier_name = "Apple";
    private String supplier_email = "apple@apple.com";
    private String supplier_phone = "+1 (123) 456-7890";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertProduct(productName,productPrice,quantity,supplier_name,supplier_email,supplier_phone);
        displayAllProducts(readAllProducts());
        displaySpecificProduct(readSpecificProduct(productName));

    }

    private void displayAllProducts(Cursor cursor){
        if (cursor != null && cursor.getCount() >0)
        {
            int pro_name,pro_price,pro_quantity,pro_sup_name,pro_sup_email,pro_sup_phone;
            pro_name = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME);
            pro_price = cursor.getColumnIndex(projection[2]);
            pro_quantity = cursor.getColumnIndex(projection[3]);
            pro_sup_name = cursor.getColumnIndex(projection[4]);
            pro_sup_email= cursor.getColumnIndex(projection[5]);
            pro_sup_phone= cursor.getColumnIndex(projection[6]);

            cursor.moveToFirst();

            while (cursor.moveToNext()) {
                Log.d(getClass().getName(), "Product Name: " + cursor.getString(pro_name));
                Log.d(getClass().getName(), "Product Price: " + cursor.getLong(pro_price));
                Log.d(getClass().getName(), "Product Quantity: " + cursor.getLong(pro_quantity));
                Log.d(getClass().getName(), "Product Supplier Name: " + cursor.getString(pro_sup_name));
                Log.d(getClass().getName(), "Product Supplier Email: " + cursor.getString(pro_sup_email));
                Log.d(getClass().getName(), "Product Supplier Phone: " + cursor.getString(pro_sup_phone));
            }
        }
        cursor.close();
    }

    private void displaySpecificProduct(Cursor cursor){
        if (cursor != null && cursor.getCount() >0)
        {
            int _id,pro_name,pro_price,pro_quantity,pro_sup_name,pro_sup_email,pro_sup_phone;
            _id = cursor.getColumnIndex(projection[0]);
            pro_name = cursor.getColumnIndex(projection[1]);
            pro_price = cursor.getColumnIndex(projection[2]);
            pro_quantity = cursor.getColumnIndex(projection[3]);
            pro_sup_name = cursor.getColumnIndex(projection[4]);
            pro_sup_email= cursor.getColumnIndex(projection[5]);
            pro_sup_phone= cursor.getColumnIndex(projection[6]);

            cursor.moveToFirst();
            Log.d(getClass().getName(),"Product Name_1: " + cursor.getString(pro_name));
            Log.d(getClass().getName(),"Product Price_1: " + cursor.getLong(pro_price));
            Log.d(getClass().getName(),"Product Quantity_1: " + cursor.getLong(pro_quantity));
            Log.d(getClass().getName(),"Product Supplier Name_1: " + cursor.getString(pro_sup_name));
            Log.d(getClass().getName(),"Product Supplier Email_1: " + cursor.getString(pro_sup_email));
            Log.d(getClass().getName(),"Product Supplier Phone_1: " + cursor.getString(pro_sup_phone));

          int i =  updateSpecificProduct(_id,"IPhone",cursor.getLong(pro_price),cursor.getLong(pro_quantity),
                    cursor.getString(pro_sup_name), cursor.getString(pro_sup_email), cursor.getString(pro_sup_phone));

          if (i >0){
              Log.d(getClass().getName(),"Update Successfull");
              displaySpecificProduct(readSpecificProduct("IPhone"));
          } else {
              Log.d(getClass().getName(),"Update Failed");
          }

        }
        cursor.close();
    }

    private Cursor readAllProducts(){
        return getContentResolver().query(InventoryContract.InventoryEntry.CONTENT_URI,
                projection, null, null, null);
    }

    private Cursor readSpecificProduct(String productName){
        return getContentResolver().query(InventoryContract.InventoryEntry.CONTENT_URI,
                projection, InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME + "=?",
                new String[]{productName},null);
    }

    private int updateSpecificProduct(int _id,String productName,long price,long quantity, String supplierName,
                                      String supplierEmail, String supplierPhone){

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,productName);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_PRICE,price);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_QUANTITY,quantity);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,supplierName);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL,supplierEmail);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE,supplierPhone);


        return getContentResolver().update(InventoryContract.InventoryEntry.CONTENT_URI, values,
                 InventoryContract.InventoryEntry._ID + "=?",new String[]{String.valueOf(_id)});
    }


    private boolean insertProduct(String productName,long price,long quantity, String supplierName,
                                  String supplierEmail, String supplierPhone) {


            ContentValues values = new ContentValues();
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,productName);
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_PRICE,price);
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_QUANTITY,quantity);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,supplierName);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL,supplierEmail);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE,supplierPhone);

            Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI,values);

            if (newUri != null){
                Log.d(getClass().getName(),"Data Inserted successfully");
                return true;
            }
            else
                Log.d(getClass().getName(),"Data Insertion failed");

        return false;
    }
}
