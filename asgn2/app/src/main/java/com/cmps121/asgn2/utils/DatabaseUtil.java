package com.cmps121.asgn2.utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cmps121.asgn2.models.Item;

public class DatabaseUtil extends SQLiteOpenHelper {
    // http://whats-online.info/science-and-tutorials/127/Android-SQLite-database-step-by-step-tutorial-for-beginners/

    // Database Version
    private static final int DATABASE_VERSION   = 1;

    // Database Name
    private static final String DATABASE_NAME   = "asgn2_database";

    // Table Name
    private static final String TABLE_NAME      = "db_items";

    // Items Table Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BITMAP = "bitmap";


    public DatabaseUtil(Context context)  {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db)  {
        String CREATE_TABLE     = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT DEFAULT \'Title\',"
                + KEY_BITMAP + " BLOB NOT NULL" + ")";

        db.execSQL(CREATE_TABLE);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Delete old table

        onCreate(db); // recreate tables
    }

    /**
     * Some CRUD Operations
     */

    public void addItem(Item item)  {
        Bitmap bitmap = item.getBitmap();
        String title = item.getTitle();

        // convert bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] buffer = byteArrayOutputStream.toByteArray();

        // open database for writing
        SQLiteDatabase db = this.getWritableDatabase();

        // start transaction
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_TITLE, title);
            values.put(KEY_BITMAP, buffer);

            // insert item
            long i = db.insert(TABLE_NAME, null, values);

            Log.i("addItem", i + "");

            // successfully added
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally
        {
            // end transaction
            db.endTransaction();

            // close database
            db.close();
        }
    }

    public boolean deleteItem(Item item) {
        int id = item.getID();
        String title = item.getTitle();

        boolean response = true;

        // open database for writing
        SQLiteDatabase db = this.getWritableDatabase();

        // start transaction
        db.beginTransaction();

        try {
            if(getItemById(id) == null && getItemByTitle(title) == null)
                return false;

            String deleteQuery = "DELETE FROM " + TABLE_NAME
                    + " WHERE " + KEY_ID + " = " + id
                    + " OR " + KEY_TITLE + " = " + title;

            // remove item
            db.execSQL(deleteQuery);

            // successfully deleted
            db.setTransactionSuccessful();
        }
        catch(SQLiteException e) {
            e.printStackTrace();
            response = false;
        }
        finally {
            // end transaction
            db.endTransaction();

            // close database
            db.close();
        }
        return response;
    }

    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<Item>();

        // open database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // start transaction
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME;

            Cursor cursor = db.rawQuery(selectQuery, null);

            // loop through all rows and add to list
            if(cursor.moveToFirst()) {
                do {
                    // convert blob data to byte array
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_BITMAP));

                    // convert byte array to bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                    // initialize new item
                    Item item = new Item();
                    item.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                    item.setBitmap(bitmap);

                    // add item to list
                    itemList.add(item);
                } while(cursor.moveToNext());
            }
            // successfully queried
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            // end transaction
            db.endTransaction();

            // close database
            db.close();
        }
        return itemList;
    }

    public List<Item> getRangeItems(int start, int end) {

        List<Item> itemList = new ArrayList<Item>();

        // open database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // start transaction
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + KEY_ID + " >= " + start
                    + " AND " + KEY_ID + " <= " + end;

            Cursor cursor = db.rawQuery(selectQuery, null);

            // loop through all rows and add to list
            if(cursor.moveToFirst()) {
                do {
                    // convert blob data to byte array
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_BITMAP));

                    // convert byte array to bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                    // initialize new item
                    Item item = new Item();
                    item.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                    item.setBitmap(bitmap);

                    // add item to list
                    itemList.add(item);
                } while(cursor.moveToNext());
            }
            // successfully queried
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            // end transaction
            db.endTransaction();

            // close database
            db.close();
        }
        return itemList;
    }

    public Item getItemById(int id){
        Item item = new Item();

        // open database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + KEY_ID + " = " + id;

            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                // convert blob data to byte array
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_BITMAP));

                // convert byte array to bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                item.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                item.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                item.setBitmap(bitmap);
            }
            // successfully queried
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            // end transaction
            db.endTransaction();

            // close database
            db.close();
        }
        return item;
    }

    public Item getItemByTitle(String title) {
        Item item = new Item();

        // open database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + KEY_TITLE + " = " + title;

            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                // convert blob data to byte array
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_BITMAP));

                // convert byte array to bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                item.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                item.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                item.setBitmap(bitmap);
            }
            // successfully queried
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            // end transaction
            db.endTransaction();

            // close database
            db.close();
        }
        return item;
    }

    public Item getItem(int id, String title) {
        Item item = new Item();

        // open database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + KEY_ID + " = " + id
                    + " AND " + KEY_TITLE + " = " + title;

            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                // convert blob data to byte array
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_BITMAP));

                // convert byte array to bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                item.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                item.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                item.setBitmap(bitmap);
            }
            // successfully queried
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            // end transaction
            db.endTransaction();

            // close database
            db.close();
        }
        return item;
    }
}
