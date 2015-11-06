package edu.cqut.cn.guahaoapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.cqut.cn.guahaoapp.edu.App.App;
import edu.cqut.cn.guahaoapp.edu.Interface.ILogin_view;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.presenter.Presenter_Login;

public class LoginActivity extends BaseActivity implements ILogin_view{

    private EditText et_userName;
    private EditText et_pwd;
//    private EditText et_vfCode;
//    private ImageView iv_vfCode;
    private Button btn_login;

    private Presenter_Login mPresenter_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        if(checkServiceisRunning()){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            this.finish();
        }


        et_userName = (EditText)findViewById(R.id.login_et_username);
        et_pwd = (EditText) findViewById(R.id.login_et_pwd);
        btn_login = (Button) findViewById(R.id.login_btn_login);

        mPresenter_Login = new Presenter_Login(this,this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        getu();
    }

    private void getu() {
        SharedPreferences sp = getSharedPreferences("main",MODE_PRIVATE);
        et_userName.setText(sp.getString("userName",""));
        et_pwd.setText(sp.getString("pwd",""));
    }

    private boolean checkServiceisRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(30);
        if(list.size()==0){
            return false;
        }else{
            for(int i = 0 ; i < list.size() ; i++){
                if(list.get(i).service.getClassName().equals("edu.cqut.cn.guahaoapp.edu.service.OrderService"))
                    return true;
            }
        }
        return false;
    }

    private void login() {
        String userName = getUserName();
        String pwd = getPwd();
        String vfCode = getVerificationCode();
        if(userName.equals("")||pwd.equals("")){
            Toast.makeText(this,"请输入登录必备信息~",Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter_Login.LoginStart();


    }

    @Override
    public void setUserName(String userName) {
        et_userName.setText(userName);
    }

    @Override
    public void setPwd(String pwd) {
        et_pwd.setText(pwd);
    }

//pwd    @Override
//    public void setVerificationCodeImage(Bitmap b) {
//        iv_vfCode.setImageBitmap(b);
//    }

    @Override
    public void setErrorMsg(String s) {
        TextView tv = (TextView)findViewById(R.id.login_tv_errorMsg);
        tv.setText(s);
        tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void loginSuccess() {
        Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();

        savepwd();

        App app = (App)getApplication();
        app.setUserName(getUserName());
        app.setPwd(getPwd());
        Intent i = new Intent();
        i.setClass(this, Setting.class);
        i.putExtra("userName", getUserName());
        startActivity(i);
        this.finish();
    }

    private void savepwd() {

        SharedPreferences sp = getSharedPreferences("main",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("userName",getUserName());
        ed.putString("pwd",getPwd());
        ed.commit();

    }

    @Override
    public void setCookie(String cookie) {
        ((App)getApplication()).setCookies(cookie);
    }

    @Override
    public void showProcessingDialog() {
        super.showProgressDialog();
    }


    @Override
    public void dismisProcessingDialog() {
        super.closeProcessDialog(0);
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }

    @Override
    public String getVerificationCode() {
//        return et_vfCode.getText().toString();
        return "";
    }

    @Override
    public String getUserName() {
        return et_userName.getText().toString();
    }

    @Override
    public String getPwd() {
        return et_pwd.getText().toString();
    }


}
