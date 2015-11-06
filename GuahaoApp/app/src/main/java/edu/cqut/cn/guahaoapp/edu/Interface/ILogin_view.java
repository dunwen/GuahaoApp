package edu.cqut.cn.guahaoapp.edu.Interface;

/**
 * Created by dun on 2015/10/13.
 */
public interface ILogin_view {
    void setUserName(String userName);
    void setPwd(String pwd);
//    void setVerificationCodeImage(Bitmap b);
    void setErrorMsg(String s);
    void loginSuccess();
    void setCookie(String cookie);
    void showProcessingDialog();
    void dismisProcessingDialog();
    String getVerificationCode();
    String getUserName();
    String getPwd();
    void showToast(String msg);

}
