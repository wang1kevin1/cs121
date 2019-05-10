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

public class DatabaseUtil extends SQLiteOpenHelper {

    public static final String DATABASE_NAME    = "database";
    public static final String TABLE_NAME       = "db_images";
    public static final int DATABASE_VERSION    = 1;
    public static final String CREATE_TABLE     = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +
            "(db_id INTEGER PRIMARY KEY AUTOINCREMENT, image BLOB NOT NULL, title TEXT DEFAULT 'Title')";
    public static final String DELETE_TABLE     = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseUtil(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)  {
        db.execSQL(CREATE_TABLE);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DELETE_TABLE); // Delete old table

        onCreate(db); // recreate tables
    }

    public void insertBitmap(Bitmap bitmap, String title)  {

        // Convert the image into byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] buffer = byteArrayOutputStream.toByteArray();

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();

        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put("image", buffer);
            values.put("title", title);
            // Insert Row
            long i = db.insert(TABLE_NAME, null, values);
            Log.i("Insert", i + "");
            // Insert into database successfully.
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally
        {
            // End the transaction.
            db.endTransaction();

            // Close database
            db.close();
        }
    }

    public boolean deleteBitmap(int id, String title) {

        boolean response = true;

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();

        // Start the transaction
        db.beginTransaction();

        try {
            if(getBitmapById(id) == null && getBitmapByTitle(title) == null)
                return false;

            String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE db_id = " + id + " OR title = " + title;
            db.execSQL(deleteQuery);
            db.setTransactionSuccessful();
        }
        catch(SQLiteException e) {
            e.printStackTrace();
            response = false;
        }
        finally {

            // End the transaction
            db.endTransaction();

            // Close database
            db.close();
        }
        return response;
    }

    public List<Bitmap> getAllBitmaps() {

        List<Bitmap> bitmaps = new ArrayList<>();

        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {

                    // Convert blob data to byte array
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("image"));

                    // Convert the byte array to Bitmap
                    bitmaps.add(BitmapFactory.decodeByteArray(blob, 0, blob.length));
                }

            }
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e) {
            e.printStackTrace();

        }
        finally {
            // End the transaction.
            db.endTransaction();

            // Close database
            db.close();
        }
        return bitmaps;
    }

    public List<Bitmap> getBitmaps(int start, int end) {

        List<Bitmap> bitmaps = new ArrayList<>();

        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE db_id >= " + start + " AND db_id <= " + end;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {

                    // Convert blob data to byte array
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("image"));

                    // Convert the byte array to Bitmap
                    bitmaps.add(BitmapFactory.decodeByteArray(blob, 0, blob.length));
                }

            }
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e) {
            e.printStackTrace();

        }
        finally {
            // End the transaction.
            db.endTransaction();

            // Close database
            db.close();
        }
        return bitmaps;
    }

    public Bitmap getBitmapById(int id){

        Bitmap bitmap = null;

        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // Start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE db_id = " + id;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    // Convert blob data to byte array
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("image"));

                    // Convert the byte array to Bitmap
                    bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                }

            }
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e) {
            e.printStackTrace();

        }
        finally {
            // End the transaction.
            db.endTransaction();

            // Close database
            db.close();
        }
        return bitmap;

    }

    public Bitmap getBitmapByTitle(String title) {
        Bitmap bitmap = null;

        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // Start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE title = " + title;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    // Convert blob data to byte array
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("image"));

                    // Convert the byte array to Bitmap
                    bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                }

            }
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e) {
            e.printStackTrace();

        }
        finally {
            // End the transaction.
            db.endTransaction();

            // Close database
            db.close();
        }
        return bitmap;

    }

}
