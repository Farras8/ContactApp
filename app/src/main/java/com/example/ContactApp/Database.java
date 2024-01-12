package com.example.ContactApp;

public class Database {

    public static final String DATABASE_NAME = "CONTACT_APP_DB";
    public static final int DATABASE_VERSION = 7;

    // Group table
    public static final String TBL_NAME = "TABLE_GROUP";
    public static final String group_Id = "group_id";
    public static final String group_Image = "group_Image";
    public static final String group_Name = "group_Name";

    public static final String CRT_TABLE = "CREATE TABLE " + TBL_NAME + "("
            + group_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + group_Image + " TEXT, "
            + group_Name + " TEXT "
            + " );";

    // Contact table
    public static final String TABLE_NAME = "CONTACT_TABLE";
    public static final String contact_Id = "Id";
    public static final String contact_Image = "Image";
    public static final String contact_Name = "Name";
    public static final String contact_LastName = "LastName";
    public static final String contact_Phone = "Phone";
    public static final String contact_Email = "Email";
    public static final String contact_Address = "Address";
    public static final String contact_Group_Id = "group_id"; // New foreign key

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + contact_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + contact_Image + " TEXT, "
            + contact_Name + " TEXT, "
            + contact_LastName + " TEXT, "
            + contact_Phone + " TEXT, "
            + contact_Email + " TEXT, "
            + contact_Address + " TEXT, "
            + contact_Group_Id + " INTEGER NULL, " // Define group_id as a foreign key
            + "FOREIGN KEY (" + contact_Group_Id + ") REFERENCES " + TBL_NAME + "(" + group_Id + ")"
            + " );";
}
