package edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.presenter;

import android.content.Context;
import android.os.AsyncTask;

import edu.cqut.cn.guahaoapp.edu.Interface.ILogin_presenter;
import edu.cqut.cn.guahaoapp.edu.Interface.ILogin_view;
import edu.cqut.cn.guahaoapp.edu.Interface.Ilogin_model;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.model.model_Login;

/**
 * Created by dun on 2015/10/13.
 */
public class Presenter_Login implements ILogin_presenter{
    Ilogin_model ilogin_model;
    ILogin_view iLogin_view;

    public Presenter_Login(ILogin_view iLogin_view,Context mContext){
        this.iLogin_view = iLogin_view;
        ilogin_model = new model_Login(mContext);
    }

    @Override
    public void LoginStart() {
        class mAsync extends AsyncTask<String,String,String>{

            @Override
            protected void onPreExecute() {
                iLogin_view.showProcessingDialog();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                return ilogin_model.model_Login(iLogin_view.getUserName(),iLogin_view.getPwd());
            }

            @Override
            protected void onPostExecute(String s) {


                if(s.equals("WRONG_USERNAME_OR_PWD")){
                    iLogin_view.setErrorMsg("用户名密码错误");
                }else if(s.equals("NO_NETWORK")){
                    iLogin_view.showToast("no network");
                }else{
                    iLogin_view.loginSuccess();
                    iLogin_view.setCookie(ilogin_model.getSessionId());
                }

                iLogin_view.dismisProcessingDialog();

                super.onPostExecute(s);
            }
        }

        new mAsync().execute();



    }

//    @Override
//    public void setVFcode() {
//
//        iLogin_view.setPwd("qq1501000255");
//        iLogin_view.setUserName("18680752457");
//        new Myasync().execute();
//    }
//
//    private  class Myasync extends AsyncTask<Void, Void, Bitmap> {
//
//
//        @Override
//        protected  Bitmap doInBackground(Void... params) {
//            Bitmap bitmap = ilogin_model.getVFcodeImage();
//            return bitmap;
//        }
//
//
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            iLogin_view.setVerificationCodeImage(bitmap);
//            super.onPostExecute(bitmap);
//        }
//    }

}
