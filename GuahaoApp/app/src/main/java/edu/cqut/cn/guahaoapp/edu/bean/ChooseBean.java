package edu.cqut.cn.guahaoapp.edu.bean;

/**
 * Created by dun on 2015/10/17.
 */
public class ChooseBean {
    private String _id = "";
    private String name = "";

    /**
     * 1:province;2:city;3:hospital;4:room;5:doctor
     * */
    private int Type = -1;

    public ChooseBean() {
    }

    public ChooseBean(String _id, String name,int type) {
        this._id = _id;
        this.name = name;
        this.Type = type;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        if(Type==1){
            return "省份";
        }else if(Type==2){
            return "城市";
        }else if(Type == 3){
            return "医院";
        }else if(Type == 4){
            return "科室";
        }else if(Type==5){
            return "医生";
        }else {
            return Type+"";
        }


    }

    public void setType(int type) {
        Type = type;
    }
}
