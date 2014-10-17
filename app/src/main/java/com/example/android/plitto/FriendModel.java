package com.example.android.plitto;

import android.graphics.drawable.Drawable;

/**
 * Created by batman on 18/10/14.
 */
public class FriendModel {

    private String name;
    private String fbuid;
    private String things;
    private String shared;
    private String dittoable;
    private String lists;
    private String sharedlists;

    public Drawable getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(Drawable profile_url) {
        this.profile_url = profile_url;
    }

    private Drawable profile_url;






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFbuid() {
        return fbuid;
    }

    public void setFbuid(String fbuid) {
        this.fbuid = fbuid;
    }

    public String getThings() {
        return things;
    }

    public void setThings(String things) {
        this.things = things;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getDittoable() {
        return dittoable;
    }

    public void setDittoable(String dittoable) {
        this.dittoable = dittoable;
    }

    public String getSharedlists() {
        return sharedlists;
    }

    public void setSharedlists(String sharedlists) {
        this.sharedlists = sharedlists;
    }

    public String getLists() {
        return lists;
    }

    public void setLists(String lists) {
        this.lists = lists;
    }

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
