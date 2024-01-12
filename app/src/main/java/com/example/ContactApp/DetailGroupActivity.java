package com.example.ContactApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DetailGroupActivity extends AppCompatActivity {

    private ListView CDGListRv;
    private TextView cGroupDetail;
    private ImageView profileGroup;
    private String id;

    private AdapterCDG adapterCDG;
    private ModelContact modelContact;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("groupId");

        cGroupDetail = findViewById(R.id.cGroupDetail);
        profileGroup = findViewById(R.id.profileGroup);
        CDGListRv = findViewById(R.id.CDGListRv);

        updateContactList();
        loadData();
    }
    private void loadData(){
        String selectQuery = "SELECT * FROM "+Database.TBL_NAME + " WHERE " + Database.group_Id + " =\"" + id + "\"";

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if(cursor.moveToFirst()){
            do{
                String name = ""+cursor.getString(cursor.getColumnIndexOrThrow(Database.group_Name));
                String image = ""+cursor.getString(cursor.getColumnIndexOrThrow(Database.group_Image));

                cGroupDetail.setText(name);

                if(image.equals("null") || image == null){
                    profileGroup.setImageResource(R.drawable.baseline_group_24);
                }else {
                    profileGroup.setImageURI(Uri.parse(image));
                }
            }while (cursor.moveToNext());
        }
        db.close();
    }
    private void updateContactList() {
        // Retrieve the list of contacts for the selected group from the database
        ArrayList<ModelContact> contactList = databaseHelper.getContactsByGroupId(id);

        // Initialize and set the AdapterCDG with the updated contact list
        adapterCDG = new AdapterCDG(this, contactList);
        CDGListRv.setAdapter(adapterCDG);
    }

    public void backToMyGroups(View v) {
        Intent i = new Intent(this, MyGroupsActivity.class);
        startActivity(i);
    }

}
