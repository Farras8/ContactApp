package com.example.ContactApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;

public class CreateContactActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText inputName, inputPhoneNum,  inputEmail, inputAddress, inputLastName;
    private String name, phone, email, address, groupId, lastName;
    private Button saveContact;
    private Spinner inputGN;


    private Uri imageUri;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        databaseHelper = new DatabaseHelper(this);


        profileImage = findViewById(R.id.profileImage);
        inputName = findViewById(R.id.inputName);
        inputPhoneNum = findViewById(R.id.inputPhoneNum);
        inputEmail = findViewById(R.id.inputEmail);
        inputAddress = findViewById(R.id.inputAddress);
        inputLastName = findViewById(R.id.inputLastName);
        saveContact = findViewById(R.id.saveContact);

        inputGN = findViewById(R.id.inputGN);
        loadGroupSpinner();

        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicker();
            }
        });
    }

    private void showImagePicker() {
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024) // ukuran kompresi dalam KB (opsional)
                .maxResultSize(1080, 1080) // ukuran maksimum gambar hasil crop (opsional)
                .start();
    }

    public void saveData() {
        name = inputName.getText().toString();
        lastName = inputLastName.getText().toString();
        phone = inputPhoneNum.getText().toString();
        email = inputEmail.getText().toString();
        address = inputAddress.getText().toString();


        if (!name.isEmpty() && !lastName.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
            if (!email.isEmpty() && !isValidEmail(email)) {
                // Show an alert if the email format is incorrect
                showAlert("Invalid Email Format");
            } else {
                if (isValidPhoneNumber(phone)) {
                    long id = databaseHelper.addContact(
                            "" + imageUri,
                            "" + name,
                            "" + lastName,
                            "" + phone,
                            "" + email,
                            "" + address,
                            "" + groupId
                    );

                    Toast.makeText(getApplicationContext(), "Create Successfully ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    // Finish the current activity (CreateContact)
                    finish();
                } else {
                    // Show an alert if the phone number format is incorrect
                    showAlert("Invalid Phone Number Format");
                }
            }
        } else {
            // Show a toast if other fields are not filled correctly
            Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        // Define the email pattern using regex
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Check if the provided email matches the pattern
        return email.matches(emailPattern);
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Define the phone number pattern using regex
        String phonePattern = "^08\\d{9,}$";

        // Check if the provided phone number matches the pattern
        return phoneNumber.matches(phonePattern);
    }
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or add any action you want
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ImagePicker.REQUEST_CODE) {
                // Mengambil gambar yang dipilih dari ImagePicker
                imageUri = data.getData();

                // Menampilkan gambar yang dipilih pada ImageView
                profileImage.setImageURI(imageUri);
            }
        }
    }
    private void loadGroupSpinner() {
        ArrayList<ModelGroup> groupList = databaseHelper.getAllGroups();

        // Tambahkan default item "Select Group Name" pada awal daftar
        ModelGroup defaultGroup = new ModelGroup("", "  --Select Group Name--  ", "");
        groupList.add(0, defaultGroup);

        ArrayAdapter<ModelGroup> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputGN.setAdapter(adapter);

        inputGN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Jika item yang dipilih adalah default item, set groupId sebagai null
                if (position == 0) {
                    groupId = null;
                } else {
                    ModelGroup selectedGroup = (ModelGroup) parentView.getSelectedItem();
                    groupId = selectedGroup.getGroupId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    public void backToMainActivity(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}