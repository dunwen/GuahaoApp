package edu.cqut.cn.guahaoapp.edu.bean;

import java.util.HashMap;

/**
 * Created by dun on 2015/10/20.
 */
public class Patient {
    private HashMap<String,String> dataMap;
    private String name ="";
    private String phone = "";
    private String id = "";
    private String IdCard = "";
    private String sex = "";
    private String age = "";

    public String getIdCard() {
        return IdCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setIdCard(String idCard) {
        IdCard = idCard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Patient() {
    }

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
