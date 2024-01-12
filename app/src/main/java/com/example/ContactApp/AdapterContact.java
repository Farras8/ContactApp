package com.example.ContactApp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AdapterContact extends BaseAdapter {

    private Context context;
    private ArrayList<ModelContact> contactList;
    private DatabaseHelper databaseHelper;

    public AdapterContact(Context context, ArrayList<ModelContact> contactList) {
        this.context = context;
        this.contactList = contactList;
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_contact_item, parent, false);
        }

        ModelContact modelContact = contactList.get(position);

        String id = modelContact.getId();
        String image = modelContact.getImage();
        String name = modelContact.getName();
        String phone = modelContact.getPhone();
        String email = modelContact.getEmail();
        String address = modelContact.getAddress();

        ImageView contactImage = convertView.findViewById(R.id.contact_image);
        TextView contactName = convertView.findViewById(R.id.contact_name);
        ImageView contactDelete = convertView.findViewById(R.id.contact_delete);
        ImageView contactEdit = convertView.findViewById(R.id.contact_edit);

        contactName.setText(name);
        if(image == null || image.equals("null"))  {
            contactImage.setImageResource(R.drawable.baseline_person_24);
        }else{
            contactImage.setImageURI(Uri.parse(image));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailContactActivity.class);
                intent.putExtra("contactId",id);
                context.startActivity(intent);
            }
        });

        contactEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditContactActivity.class);
                intent.putExtra("ID",id);
                intent.putExtra("NAME",name);
                intent.putExtra("PHONE",phone);
                intent.putExtra("EMAIL",email);
                intent.putExtra("ADDRESS",address);
                intent.putExtra("IMAGE",image);

                intent.putExtra("isEditMode",true);
                context.startActivity(intent);
            }
        });

        contactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(id);
            }
        });

        return convertView;
    }

    private void showDeleteConfirmationDialog(String contactId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this contact?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "Yes", delete the contact
                databaseHelper.deleteContact(contactId);
                ((MainActivity) context).onResume();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "No", do nothing
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
