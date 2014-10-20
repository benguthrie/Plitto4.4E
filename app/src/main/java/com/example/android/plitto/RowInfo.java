
package com.example.android.plitto;

/**
 * Created by batman on 11/10/14.
 */
public class RowInfo {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String name;
    private String date;
    private String mykey;
    private String uid;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getMykey() {
        return mykey;
    }

    public void setMykey(String mykey) {
        this.mykey = mykey;
    }


    public RowInfo(int id, String name, String date, String mykey, String uid) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.mykey = mykey;
        this.uid = uid;
    }

    public RowInfo(int id, String name) {
        this.id = id;
        this.name = name;
        this.date = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
