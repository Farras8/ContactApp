package com.example.ContactApp;

public class ModelGroup {

    private String groupId;
    private String groupName;
    private String groupImage;

    public ModelGroup(String groupId, String groupName, String groupImage) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupImage = groupImage;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    @Override
    public String toString() {
        return groupName;
    }

}
