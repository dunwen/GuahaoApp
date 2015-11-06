package edu.cqut.cn.guahaoapp.edu.Interface;

import java.net.MalformedURLException;

/**
 * Created by dun on 2015/10/13.
 */
public interface Ilogin_model {
    String getVFcodeImage() throws MalformedURLException, Exception;
    String getSessionId();
    String model_Login(final String userName,final String pwd);
}
