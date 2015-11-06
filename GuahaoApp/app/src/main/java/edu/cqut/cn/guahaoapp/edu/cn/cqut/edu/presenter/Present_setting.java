package edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.presenter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.cqut.cn.guahaoapp.edu.Interface.Isetting;
import edu.cqut.cn.guahaoapp.edu.Interface.Isetting_model;
import edu.cqut.cn.guahaoapp.edu.Interface.Isetting_presenter;
import edu.cqut.cn.guahaoapp.edu.bean.ChooseBean;
import edu.cqut.cn.guahaoapp.edu.bean.Datapick;
import edu.cqut.cn.guahaoapp.edu.bean.Patient;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.model.model_setting;

/**
 * Created by dun on 2015/10/17.
 */
public class Present_setting implements Isetting_presenter{

    Isetting mIsetting_view;
    Isetting_model mIsetting_model;


   public Present_setting(Isetting mIsetting_view){
       this.mIsetting_model = new model_setting();
       this.mIsetting_view = mIsetting_view;
   }

    @Override
    public void setProvinceList() {
        class myAsync extends AsyncTask<String,String,String>{

            @Override
            protected void onPreExecute() {

                mIsetting_view.showProcessDialog();

                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                return mIsetting_model.getProvince();
            }

            @Override
            protected void onPostExecute(String s) {
                if(s.equals("NO_NETWORK")){
                    mIsetting_view.dismisProcessDialog();
                    mIsetting_view.showToast("no network");
                    return;
                }

                dealWithJson(s,1);
                mIsetting_view.dismisProcessDialog();
                super.onPostExecute(s);
            }
        }

        new myAsync().execute();

    }

        private void dealWithJson(String jsonString,int Type){
            Log.i("province",jsonString);

            ArrayList<ChooseBean> list = new ArrayList<ChooseBean>();
            try {

                JSONArray jsonArray = new JSONArray(jsonString);

                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    ChooseBean c = new ChooseBean();
                    c.setType(Type);
                    c.set_id(jsonObject.getString("value"));
                    c.setName(jsonObject.getString("text"));
                    list.add(c);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(Type==1){
                mIsetting_view.setProvinces(list);
            }else if(Type==2){
                mIsetting_view.setCitys(list);
            }else if(Type==3){
                mIsetting_view.setHospital(list);
            }
        }


    @Override
    public void setCityList(final String provinceId) {
        class myAsync extends AsyncTask<String,String,String>{

            @Override
            protected void onPreExecute() {
                mIsetting_view.showProcessDialog();
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                if(s.equals("NO_NETWORK")){
                    mIsetting_view.dismisProcessDialog();
                    mIsetting_view.showToast("no network");
                    return;
                }


                dealWithJson(s,2);
                mIsetting_view.dismisProcessDialog();
                super.onPostExecute(s);

            }

            @Override
            protected String doInBackground(String... params) {
                return mIsetting_model.getcity(provinceId);
            }
        }
        new myAsync().execute();



    }

    @Override
    public void setHospitalList(final String provinceId, final String cityId) {
        class getHostpitalAsync extends AsyncTask<String,String,String>{

            @Override
            protected void onPreExecute() {
                mIsetting_view.showProcessDialog();

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                if(s.equals("NO_NETWORK")){
                    mIsetting_view.dismisProcessDialog();
                    mIsetting_view.showToast("no network");
                    return;
                }

                dealWithJson(s, 3);
                mIsetting_view.dismisProcessDialog();
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                return mIsetting_model.getHospital(provinceId, cityId);
            }
        }
        new getHostpitalAsync().execute();

    }

    @Override
    public void setRoomList(final String hospitalId) {
        class getRooListAsync extends  AsyncTask<String,String,String>{

            @Override
            protected void onPreExecute() {
                mIsetting_view.showProcessDialog();
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                if(s.equals("NO_NETWORK")){
                    mIsetting_view.dismisProcessDialog();
                    mIsetting_view.showToast("no network");
                    return;
                }

                dealWithJsonRoom(s);

                mIsetting_view.dismisProcessDialog();
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                return mIsetting_model.getRoom(hospitalId);
            }
        }
        new getRooListAsync().execute();

    }

    @Override
    public void setDoctorList(final String roomId, final String cookie) {
        class setdoctorAsync extends AsyncTask<String,String,String>{

            ArrayList<ChooseBean> list;

            @Override
            protected void onPostExecute(String s) {
                if(list==null){
                    mIsetting_view.dismisProcessDialog();
                    mIsetting_view.showToast("no network");
                    return;
                }


                mIsetting_view.setDoctor(list);
                mIsetting_view.dismisProcessDialog();
                super.onPostExecute(s);
            }

            @Override
            protected void onPreExecute() {
                mIsetting_view.showProcessDialog();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                this.list = mIsetting_model.getDoctor(roomId,cookie);
                return "";
            }
        }
        new setdoctorAsync().execute();
    }

    @Override
    public void setorderType() {

    }



    private void dealWithJsonRoom(String s){
        ArrayList<ChooseBean> list = new ArrayList<ChooseBean>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("hospDepts");

            for(int i = 0; i<jsonArray.length();i++){
                JSONObject tempObject = (JSONObject) jsonArray.opt(i);
                JSONArray array = tempObject.getJSONArray("obj");

                for(int j = 0 ; j<array.length();j++){
                    JSONObject object = (JSONObject) array.opt(j);
                    ChooseBean c = new ChooseBean();
                    c.setType(4);
                    c.set_id(object.getString("value"));
                    c.setName(object.getString("text"));
                    list.add(c);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIsetting_view.setRoomType(list);



    }

    @Override
    public void setDateList(final String doctorId,final String cookie) {
        class getDateAsync extends AsyncTask<String,String,String>{

            @Override
            protected void onPreExecute() {
                mIsetting_view.showProcessDialog();
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                if(s.equals("NO_NETWORK")){
                    mIsetting_view.dismisProcessDialog();
                    mIsetting_view.showToast("no network");
                    return;
                }

                dealWithJsonDate(s);
                mIsetting_view.dismisProcessDialog();
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                return mIsetting_model.getDate(doctorId,cookie);
            }
        }
        new getDateAsync().execute();

    }

    @Override
    public void setPatient(final String url,final String cookie) {
        class setPatientAsync extends AsyncTask<String,String,String>{
            ArrayList<Patient> list;

            @Override
            protected void onPostExecute(String s) {
                if(list==null){
                    mIsetting_view.dismisProcessDialog();
                    mIsetting_view.setPatiner(null);
                    return;
                }

                mIsetting_view.setPatiner(list);
                mIsetting_view.dismisProcessDialog();

                super.onPostExecute(s);
            }

            @Override
            protected void onPreExecute() {
                mIsetting_view.showProcessDialog();

                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                list = mIsetting_model.getPatient(url,cookie);
                return "";
            }
        }

        new setPatientAsync().execute();

    }

    private void dealWithJsonDate(String s) {
        Log.i("date",s);
        ArrayList<Datapick> list = new ArrayList<>();
        try {
            JSONArray tempArray = new  JSONArray(s);
            JSONObject tempObject = ((JSONObject) tempArray.get(0)).getJSONObject("scheduleData");
            JSONArray array = tempObject.getJSONArray("schedules");

            for(int i = 0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                String date = object.getString("date");
                String morningString = object.getString("morning");
                String afternoonString = object.getString("afternoon");
                String eveningString = object.getString("evening");

                if(!morningString.equals("null")){
                    JSONObject jo = object.getJSONObject("morning");
                    Datapick d = new Datapick();
                    d.setAmorPm("早上");
                    int type =jo.getInt("type");
                    String clinicType = jo.getString("clinicType");

                    if(!(type==2||clinicType.equals("专家门诊"))){
                        continue;
                    }

                    d.setClinicType(clinicType);
                    d.setState(jo.getInt("status"));
                    d.setType(type);
                    d.setUrl(jo.getString("url"));
                    d.setDate(date);

                    list.add(d);
                }

                if(!afternoonString.equals("null")){
                    JSONObject jo = object.getJSONObject("afternoon");
                    Datapick d = new Datapick();
                    d.setAmorPm("下午");
                    int type =jo.getInt("type");
                    String clinicType = jo.getString("clinicType");

                    if(!(type==2||clinicType.equals("专家门诊"))){
                        continue;
                    }

                    d.setClinicType(clinicType);
                    d.setState(jo.getInt("status"));
                    d.setType(type);
                    d.setUrl(jo.getString("url"));
                    d.setDate(date);

                    list.add(d);
                }

                if(!eveningString.equals("null")){
                    JSONObject jo = object.getJSONObject("evening");
                    Datapick d = new Datapick();
                    d.setAmorPm("晚上");
                    int type =jo.getInt("type");
                    String clinicType = jo.getString("clinicType");

                    if(!(type==2||clinicType.equals("专家门诊"))){
                        continue;
                    }

                    d.setClinicType(clinicType);
                    d.setState(jo.getInt("status"));
                    d.setType(type);
                    d.setUrl(jo.getString("url"));
                    d.setDate(date);

                    list.add(d);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIsetting_view.setDate(list);

    }

    public Isetting getmIsetting_view() {
        return mIsetting_view;
    }
}
