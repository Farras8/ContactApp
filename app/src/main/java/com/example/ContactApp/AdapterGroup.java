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

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AdapterGroup extends BaseAdapter {

    private Context context;
    private ArrayList<ModelGroup> groupList;

    private DatabaseHelper databaseHelper;

    public AdapterGroup(Context context, ArrayList<ModelGroup> groupList){
        this.context = context;
        this.groupList = groupList;
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_group_item, parent, false);
        }
        ModelGroup modelGroup = groupList.get(position);

        String Id = modelGroup.getGroupId();
        String Image = modelGroup.getGroupImage();
        String Name = modelGroup.getGroupName();

        ImageView groupImage = convertView.findViewById(R.id.group_image);
        TextView groupName = convertView.findViewById(R.id.group_name);
        ImageView groudEdit = convertView.findViewById(R.id.group_edit);
        ImageView groupDelete = convertView.findViewById(R.id.group_delete);

        groupName.setText(Name);
        if (Image == null || Image.equals("null"))  {
            groupImage.setImageResource(R.drawable.baseline_group_24);
        }else{
            groupImage.setImageURI(Uri.parse(Image));
        }
        groudEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditGroupActivity.class);
                intent.putExtra("ID",Id);
                intent.putExtra("NAME",Name);
                intent.putExtra("IMAGE",Image);

                intent.putExtra("isEditMode",true);
                context.startActivity(intent);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailGroupActivity.class);
                intent.putExtra("groupId",Id);
                context.startActivity(intent);
            }
        });

        groupDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(Id);
            }
        });
        return convertView;
    }
    private void showDeleteConfirmationDialog(String groupId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this group?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "Yes", delete the contact
                databaseHelper.deleteGroup(groupId);
                ((MyGroupsActivity) context).onResume();
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
