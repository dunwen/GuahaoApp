package edu.cqut.cn.guahaoapp.edu.App;

import android.app.Application;

/**
 * Created by dun on 2015/10/13.
 */
public class App extends Application{

    private String cookies = "";
    private String userName = "";
    private String pwd = "";

    public String getCookies() {
        return cookies;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }
}
