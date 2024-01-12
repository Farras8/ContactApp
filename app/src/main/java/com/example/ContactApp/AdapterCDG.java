package com.example.ContactApp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AdapterCDG extends BaseAdapter {

    private Context context;
    private ArrayList<ModelContact> contactList;
    private DatabaseHelper databaseHelper;

    public AdapterCDG(Context context, ArrayList<ModelContact> contactList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.row_detailgroup_item, parent, false);
        }
        ModelContact modelContact = contactList.get(position);
        String id = modelContact.getId();
        String image = modelContact.getImage();
        String name = modelContact.getName();

        ImageView contactImage = convertView.findViewById(R.id.contact_image);
        TextView contactName = convertView.findViewById(R.id.contact_name);

        contactName.setText(name);
        if(image == null || image.equals("null")) {
            contactImage.setImageResource(R.drawable.baseline_person_24);
        }else{
            contactImage.setImageURI(Uri.parse(image));
        }

        ImageView contact_delete = convertView.findViewById(R.id.contact_delete);
        contact_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the position of the clicked item
                int position = (int) v.getTag();

                // Get the ModelContact object from the adapter
                ModelContact contact = contactList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete this contact From this Group?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the contact from the group in the database
                        databaseHelper.deleteContactGroup(contact.getId());

                        // Update the contact list in the adapter
                        contactList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        contact_delete.setTag(position);
        return convertView;
    }
}
