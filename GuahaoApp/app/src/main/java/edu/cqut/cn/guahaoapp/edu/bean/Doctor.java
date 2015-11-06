package edu.cqut.cn.guahaoapp.edu.bean;

/**
 * Created by dun on 2015/10/18.
 */
public class Doctor {
    private String name = "";
    private String id ="";
    private String beGoodAt = "";
    private String iconUrl = "";

    public Doctor() {
    }

    public Doctor(String name, String id, String beGoodAt, String iconUrl) {
        this.name = name;
        this.id = id;
        this.beGoodAt = beGoodAt;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeGoodAt() {
        return beGoodAt;
    }

    public void setBeGoodAt(String beGoodAt) {
        this.beGoodAt = beGoodAt;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
