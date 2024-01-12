package com.example.ContactApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyGroupsActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private ListView groupRv;
    private EditText searchInput;
    private ImageButton searchButton;
    private SwipeRefreshLayout group_swipe;
    private DatabaseHelper databaseHelper;

    private ModelGroup modelGroup;
    private AdapterGroup adapterGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        databaseHelper = new DatabaseHelper(this);

        groupRv = findViewById(R.id.groupRv);

        floatingActionButton = findViewById(R.id.floatingActionButtonGroup);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGroupsActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });
        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchInput = findViewById(R.id.seacrhInput);
                String searchTerm = searchInput.getText().toString().toLowerCase();

                // Filter contacts
                ArrayList<ModelGroup> filteredGroups = new ArrayList<>();
                for (ModelGroup group : databaseHelper.getAllGroups()) {
                    if (group.getGroupName().toLowerCase().contains(searchTerm)) {
                        filteredGroups.add(group);
                    }
                }

                // Check if any contacts were found
                if (filteredGroups.isEmpty()) {
                    // Show "Contact not found" message
                    Toast.makeText(MyGroupsActivity.this, "Group not found", Toast.LENGTH_SHORT).show();
                } else {
                    // Update contact list
                    adapterGroup = new AdapterGroup(MyGroupsActivity.this, filteredGroups);
                    groupRv.setAdapter(adapterGroup);
                }
            }
        });
        group_swipe = findViewById(R.id.group_swipe);
        group_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh data here
                loadDataGroup();
                // Stop the refreshing indicator
                group_swipe.setRefreshing(false);
            }
        });
        loadDataGroup();
    }
    private void loadDataGroup(){
        adapterGroup = new AdapterGroup(this,databaseHelper.getAllGroups());
        groupRv.setAdapter(adapterGroup);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataGroup();
    }
    public void backToMainActivity(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}