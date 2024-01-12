package com.example.ContactApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Database.CREATE_TABLE);
        db.execSQL(Database.CRT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Database.TBL_NAME);
        onCreate(db);
    }

    public long addGroup(String image, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.group_Image, image);
        contentValues.put(Database.group_Name, name);

        long id = db.insert(Database.TBL_NAME, null, contentValues);

        db.close();

        return id;
    }
    // ... (existing code)


// ... (existing code)

    public ArrayList<ModelContact> getContactsByGroupId(String groupId) {
        ArrayList<ModelContact> contactList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Database.TABLE_NAME +
                " WHERE " + Database.contact_Group_Id + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{groupId});

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Id));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Name));
                String Lastname = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_LastName));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Image));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Phone));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Email));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Address));
                String contactGroupId = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Group_Id));

                ModelContact modelContact = new ModelContact(id, name,Lastname, image, phone, email, address, contactGroupId);
                contactList.add(modelContact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return contactList;
    }

    public ArrayList<ModelGroup> getAllGroups() {
        ArrayList<ModelGroup> arrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Database.TBL_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(Database.group_Id));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.group_Name));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(Database.group_Image));

                ModelGroup modelGroup = new ModelGroup(id, name, image);
                arrayList.add(modelGroup);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return arrayList;
    }
    public void updateGroup(String id, String image, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.group_Image, image);
        contentValues.put(Database.group_Name, name);

        db.update(Database.TBL_NAME, contentValues, Database.group_Id + " =? ", new String[]{id});

        db.close();
    }

    public void deleteGroup(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Database.TBL_NAME, Database.group_Id + " =? ", new String[]{id});
        db.close();
    }
    public long addContact(String image, String name,String LastName, String phone, String email, String address, String groupId) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.contact_Image, image);
        contentValues.put(Database.contact_Name, name);
        contentValues.put(Database.contact_LastName, LastName);
        contentValues.put(Database.contact_Phone, phone);
        contentValues.put(Database.contact_Email, email);
        contentValues.put(Database.contact_Address, address);
        contentValues.put(Database.contact_Group_Id, groupId);

        long id = db.insert(Database.TABLE_NAME, null, contentValues);

        db.close();

        return id;
    }

    public void updateContact(String id, String image,  String name, String lastName, String phone, String email, String address, String groupId) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.contact_Image, image);
        contentValues.put(Database.contact_Name, name);
        contentValues.put(Database.contact_LastName,lastName);
        contentValues.put(Database.contact_Phone, phone);
        contentValues.put(Database.contact_Email, email);
        contentValues.put(Database.contact_Address, address);
        contentValues.put(Database.contact_Group_Id, groupId);

        db.update(Database.TABLE_NAME, contentValues, Database.contact_Id+" =? ",new String[]{id});

        db.close();

    }

    public void deleteContact(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Database.TABLE_NAME, Database.contact_Id + " =? ", new String[]{id});
        db.close();
    }

    public void deleteAllContact(){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM "+Database.TABLE_NAME);
        db.close();
    }
    public ArrayList<ModelContact> getAllData() {
        ArrayList<ModelContact> arrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Database.TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Id));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Name));
                String Lastname = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_LastName));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Image));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Phone));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Email));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Address));
                String groupId = cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Group_Id));

                ModelContact modelContact = new ModelContact(id, name, Lastname, image, phone, email, address, groupId);
                arrayList.add(modelContact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return arrayList;
    }


    public void deleteContactGroup(String contactId) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + Database.TABLE_NAME + " SET " + Database.contact_Group_Id + " = null WHERE " + Database.contact_Id + " = ?";
        db.execSQL(query, new String[]{contactId});
        db.close();
    }


}