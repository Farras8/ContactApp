package com.example.ContactApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailContactActivity extends AppCompatActivity {

    private TextView cNameDetail,PhoneDetail,emailDetail,addressDetail,GNDetail,groupTitle,LastNameDetail;
    private ImageView profileImage;
    private CircleImageView callButton;
    private String groupName;
    private CircleImageView messageButton;
    private static final int REQUEST_PHONE_CALL = 1;
    private static final int REQUEST_SEND_SMS = 2;
    private String id;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");

        cNameDetail = findViewById(R.id.cNameDetail);
        PhoneDetail = findViewById(R.id.PhoneDetail);
        emailDetail = findViewById(R.id.emailDetail);
        addressDetail = findViewById(R.id.addressDetail);
        GNDetail = findViewById(R.id.GNDetail);
        profileImage = findViewById(R.id.profileImage);
        groupTitle = findViewById(R.id.groupTitle);
        LastNameDetail = findViewById(R.id.LastNameDetail);

        // ...

        callButton = findViewById(R.id.CallButton);
        messageButton = findViewById(R.id.MessageButton);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = PhoneDetail.getText().toString();
                Uri dialUri = Uri.parse("tel:" + phoneNumber);
                Intent dialIntent = new Intent(Intent.ACTION_CALL, dialUri);

                if (ContextCompat.checkSelfPermission(DetailContactActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailContactActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_PHONE_CALL);
                } else {
                    startActivity(dialIntent);
                }
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = PhoneDetail.getText().toString();
                Uri smsUri = Uri.parse("smsto:" + phoneNumber);
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
                smsIntent.putExtra("exit_on_sent", true);

                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(smsIntent, REQUEST_SEND_SMS);
                }
            }
        });

        loadDataById();
    }

    public void backToMainActivity(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void loadDataById(){
        String selectQuery = "SELECT * FROM "+Database.TABLE_NAME + " WHERE " + Database.contact_Id + " =\"" + id + "\"";

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                String name = ""+cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Name));
                String LastName = ""+cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_LastName));
                String image = ""+cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Image));
                String phone = ""+cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Phone));
                String email = ""+cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Email));
                String address = ""+cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Address));
                String groupId = ""+cursor.getString(cursor.getColumnIndexOrThrow(Database.contact_Group_Id));
                groupName = getGroupNameById(groupId);

                if (groupName == null || groupName.isEmpty()) {
                    groupTitle.setVisibility(View.GONE);
                } else {
                    groupTitle.setVisibility(View.VISIBLE);
                    groupTitle.setText("Group");
                }

                cNameDetail.setText(name);
                LastNameDetail.setText(LastName);
                PhoneDetail.setText(phone);
                emailDetail.setText(email);
                addressDetail.setText(address);
                GNDetail.setText(groupName);

                if(image.equals("null") || image == null){
                    profileImage.setImageResource(R.drawable.baseline_person_24);
                }else {
                    profileImage.setImageURI(Uri.parse(image));
                }
            }while (cursor.moveToNext());
        }
        db.close();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = PhoneDetail.getText().toString();
                Uri dialUri = Uri.parse("tel:" + phoneNumber);
                Intent dialIntent = new Intent(Intent.ACTION_CALL, dialUri);
                startActivity(dialIntent);
            } else {
                Toast.makeText(this, "Izin panggilan telepon ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getGroupNameById(String groupId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selectQuery = "SELECT " + Database.group_Name + " FROM " + Database.TBL_NAME + " WHERE " + Database.group_Id + " =\"" + groupId + "\"";
        Cursor cursor = db.rawQuery(selectQuery, null);

        String groupName = "";

        if(cursor.moveToFirst()){
            groupName = cursor.getString(cursor.getColumnIndexOrThrow(Database.group_Name));
        }

        cursor.close();
        db.close();

        return groupName;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SEND_SMS) {
            finish();
        }
    }
}