package edu.cqut.cn.guahaoapp.edu.bean;

import java.util.ArrayList;

/**
 * Created by dun on 2015/10/16.
 */
public class RoomType {
    String type = "";
    ArrayList<ChooseBean> rooms = null;


    public RoomType(String type) {
        this.type = type;
    }

    public RoomType() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ChooseBean> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<ChooseBean> rooms) {
        this.rooms = rooms;
    }

    public void addIntoList(ChooseBean room){
        if(room!=null){
            rooms.add(room);
        }


    }
}
