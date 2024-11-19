package com.example.dacn_murkoff_care_android.Model;

public class Setting {
    /*Class này thể hiện trong phần fragment_setting*/
    private int icon;
    private String id;
    private String name;

    public Setting(int icon, String id, String name) {
        this.icon = icon;
        this.id = id;
        this.name = name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
