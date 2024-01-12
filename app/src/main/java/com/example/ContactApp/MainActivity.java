package com.example.ContactApp;

import androidx.appcompat.app.AppCompatActivity;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ContactApp.databinding.ActivityMainBinding;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private TextView MyGroupBtn;
    private ListView contactRv;
    private ImageButton SortButton;
    private ImageButton DeleteAllButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText searchInput;
    private ImageButton searchButton;
    private DatabaseHelper databaseHelper;
    private ModelContact modelContact;
    private AdapterContact adapterContact;
    private boolean isAscending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        contactRv = findViewById(R.id.contactRv);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateContactActivity.class);
                startActivity(intent);
            }
        });

        MyGroupBtn = findViewById(R.id.MyGroupBtn);
        MyGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyGroupsActivity.class);
                startActivity(intent);
            }
        });

        DeleteAllButton = findViewById(R.id.DeleteAllButton);
        DeleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete All Contacts");
                builder.setMessage("Are you sure you want to delete all contacts?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete all contacts
                        databaseHelper.deleteAllContact();
                        // Refresh the contact list
                        loadData();
                        Toast.makeText(MainActivity.this, "All contacts deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
            }
        });

        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchInput = findViewById(R.id.seacrhInput);
                String searchTerm = searchInput.getText().toString().toLowerCase();
                ArrayList<ModelContact> filteredContacts = new ArrayList<>();
                for (ModelContact contact : databaseHelper.getAllData()) {
                    if (contact.getName().toLowerCase().contains(searchTerm)) {
                        filteredContacts.add(contact);
                    }
                }
                if (filteredContacts.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Contact not found", Toast.LENGTH_SHORT).show();
                } else {

                    adapterContact = new AdapterContact(MainActivity.this, filteredContacts);
                    contactRv.setAdapter(adapterContact);
                }
            }
        });

        SortButton = findViewById(R.id.SortButton);
        SortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] sortOrderOptions = {"Ascending", "Descending"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select Sort Order");
                builder.setSingleChoiceItems(sortOrderOptions, isAscending ? 0 : 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isAscending = (which == 0);
                        dialog.dismiss();


                        loadData();
                    }
                });


                builder.show();
            }
        });
        swipeRefreshLayout = findViewById(R.id.home_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh data here
                loadData();
                // Stop the refreshing indicator
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        loadData();
    }

    private void loadData() {
        ArrayList<ModelContact> contacts = databaseHelper.getAllData();
        Collator collator = Collator.getInstance();
        Comparator<ModelContact> comparator = new Comparator<ModelContact>() {
            public int compare(ModelContact contact1, ModelContact contact2) {
                return collator.compare(contact1.getName(), contact2.getName());
            }
        };

        if (isAscending) {
            Collections.sort(contacts, comparator);
        } else {
            Collections.sort(contacts, Collections.reverseOrder(comparator));
        }

        adapterContact = new AdapterContact(this, contacts);
        contactRv.setAdapter(adapterContact);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

}