package com.example.ContactApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class EditContactActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText inputName, inputPhoneNum, inputEmail, inputAddress, inputLastName;
    private Spinner inputGN;
    private String id, image, name, phone, email, address, groupId,groupName, lastName;
    private Boolean isEditMode;
    private Button saveContact;

    private Uri imageUri;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        databaseHelper = new DatabaseHelper(this);


        profileImage = findViewById(R.id.profileImage);
        inputName = findViewById(R.id.inputName);
        inputLastName = findViewById(R.id.inputLastName);
        inputPhoneNum = findViewById(R.id.inputPhoneNum);
        inputEmail = findViewById(R.id.inputEmail);
        inputAddress = findViewById(R.id.inputAddress);
        saveContact = findViewById(R.id.saveContact);
        inputGN = findViewById(R.id.inputGN);

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);

        if (!isEditMode) {
            groupId = databaseHelper.getAllGroups().get(0).getGroupId();
        }

        if (isEditMode) {
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            lastName = intent.getStringExtra("LAST_NAME");
            phone = intent.getStringExtra("PHONE");
            email = intent.getStringExtra("EMAIL");
            address = intent.getStringExtra("ADDRESS");
            image = intent.getStringExtra("IMAGE");
            groupId = intent.getStringExtra("GROUP_ID");

            inputName.setText(name);
            inputLastName.setText(lastName);
            inputPhoneNum.setText(phone);
            inputEmail.setText(email);
            inputAddress.setText(address);

            if (image != null && !image.isEmpty() && !image.equals("null")) {
                imageUri = Uri.parse(image);
                profileImage.setImageURI(imageUri);
            } else {
                profileImage.setImageResource(R.drawable.baseline_person_24);
            }
        } else {
            // ...
        }

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

        // Load spinner data
        loadGroupSpinner();
    }

    private void showImagePicker() {
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    public void saveData() {
        name = inputName.getText().toString();
        phone = inputPhoneNum.getText().toString();
        email = inputEmail.getText().toString();
        address = inputAddress.getText().toString();
        lastName = inputLastName.getText().toString();


        if (!name.isEmpty() && !lastName.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
            if (!email.isEmpty() && !isValidEmail(email)) {
                showAlert("Invalid Email Format");
            } else {
                if (isValidPhoneNumber(phone)) {
                    if (isEditMode) {
                        databaseHelper.updateContact(
                                "" + id,
                                "" + imageUri,
                                "" + name,
                                ""+ lastName,
                                "" + phone,
                                "" + email,
                                "" + address,
                                "" + groupId
                        );
                        Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        long id = databaseHelper.addContact(
                                "" + imageUri,
                                "" + name,
                                "" + lastName,
                                "" + phone,
                                "" + email,
                                "" + address,
                                "" + groupId
                        );

                        Toast.makeText(getApplicationContext(), "Inserted " + id, Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    finish();
                } else {
                    showAlert("Invalid Phone Number Format");
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "^08\\d{9,}$";
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


    public void backToMainActivity(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ImagePicker.REQUEST_CODE) {
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }
}
