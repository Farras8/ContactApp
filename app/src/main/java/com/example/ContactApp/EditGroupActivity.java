package com.example.ContactApp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class EditGroupActivity extends AppCompatActivity {

    private ImageView profileGroup;
    private EditText inputGroupName;
    private String id, image,  name;
    private Boolean isEditMode;
    private Button saveGroup;
    private String groupName;

    private Uri imageUri;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        databaseHelper = new DatabaseHelper(this);

        profileGroup = findViewById(R.id.profileGroup);
        inputGroupName = findViewById(R.id.inputGroupName);
        saveGroup = findViewById(R.id.saveGroup);

        profileGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicker();
            }
        });

        saveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGroupData();
            }
        });

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode",false);

        if(isEditMode){
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            image = intent.getStringExtra("IMAGE");

            inputGroupName.setText(name);
            if (image != null && !image.isEmpty() && !image.equals("null")) {
                imageUri = Uri.parse(image);
                profileGroup.setImageURI(imageUri);
            } else {
                profileGroup.setImageResource(R.drawable.baseline_group_24);
            }

        }else {

        }
    }

    private void saveGroupData(){
        groupName = inputGroupName.getText().toString();

        if(!groupName.isEmpty()){
            if(isEditMode){
                databaseHelper.updateGroup(
                        ""+id,
                        ""+imageUri,
                        ""+groupName
                );
                Toast.makeText(getApplicationContext(), "Updated Successfully ", Toast.LENGTH_SHORT).show();
            }else {
                long id = databaseHelper.addGroup(
                        "" + imageUri,
                        "" + groupName
                );
                Toast.makeText(getApplicationContext(), "Create Successfully ", Toast.LENGTH_SHORT).show();

            }
            Intent intent = new Intent(getApplicationContext(), MyGroupsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }
    private void showImagePicker() {
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024) // ukuran kompresi dalam KB (opsional)
                .maxResultSize(1080, 1080) // ukuran maksimum gambar hasil crop (opsional)
                .start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ImagePicker.REQUEST_CODE) {
                // Mengambil gambar yang dipilih dari ImagePicker
                imageUri = data.getData();

                // Menampilkan gambar yang dipilih pada ImageView
                profileGroup.setImageURI(imageUri);
            }
        }
    }
    public void backToMyGroups(View v){
        Intent i = new Intent(this, MyGroupsActivity.class);
        startActivity(i);
    }
}