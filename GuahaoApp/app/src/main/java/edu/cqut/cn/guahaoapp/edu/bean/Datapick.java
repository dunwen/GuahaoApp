package edu.cqut.cn.guahaoapp.edu.bean;

/**
 * Created by dun on 2015/10/19.
 */
public class Datapick {

    String date = "";
    String AmorPm ="";

    /** 状态（0-4对应["截止","停诊","已满","已满","预约"]，值为4的才是空闲预约） */
    int state = -1;

    /** （0-8对应["","专科门诊","专家门诊","膏方门诊","疑难门诊","特需门诊","精品门诊","其他门诊","普通门诊"]） */
    int type = -1;

    /** clinicType: 门诊类型（专家门诊比较特殊，在这里会有返回值"专家门诊" */
    String clinicType = "";
    String url = "";
    String dayOfWeek = "";

    public Datapick() {
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmorPm() {
        return AmorPm;
    }

    public void setAmorPm(String amorPm) {
        AmorPm = amorPm;
    }

    public int getState() {
        return state;
    }

    public String getStateString() {
        if(state==0){
            return "截止";
        }else if(state==1){
            return "停诊";
        }else if(state==2||state==3){
            return "已满";
        }else if(state ==4){
            return "可预约";
        }



        return state+"";
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClinicType() {
        return clinicType;
    }

    public void setClinicType(String clinicType) {
        this.clinicType = clinicType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
