package edu.cqut.cn.guahaoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.cqut.cn.guahaoapp.edu.App.App;
import edu.cqut.cn.guahaoapp.edu.Interface.Isetting;
import edu.cqut.cn.guahaoapp.edu.bean.ChooseBean;
import edu.cqut.cn.guahaoapp.edu.bean.Datapick;
import edu.cqut.cn.guahaoapp.edu.bean.Patient;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.Units.Md5;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.adapter.ChooseAdapter;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.adapter.DatePickAdapter;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.adapter.PatientAdapter;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.model.model_Login;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.presenter.Present_setting;
import edu.cqut.cn.guahaoapp.edu.service.OrderService;

public class Setting extends BaseActivity implements Isetting,View.OnClickListener{

    ArrayList<ChooseBean> provincesList;
    ArrayList<ChooseBean> cityList;
    ArrayList<ChooseBean> hospitalList;
    ArrayList<ChooseBean> roomTypeList;
    ArrayList<ChooseBean> doctorList;
    ArrayList<Datapick> datelist;
    ArrayList<Patient> patientsList;

    LinearLayout ll_province;
    TextView tv_province;
    LinearLayout ll_city;
    TextView tv_city;
    LinearLayout ll_hospital;
    TextView tv_hospital;
    LinearLayout ll_room;
    TextView tv_room;
    LinearLayout ll_doctor;
    TextView tv_doctor;
    LinearLayout ll_orderTyle;
    TextView tv_orderType;
    LinearLayout ll_patient;
    TextView tv_patient;

    Button btn_save;
    Button btn_changeUser;

    LinearLayout ll_hospitalArea;
    LinearLayout ll_roomArea;
    LinearLayout ll_doctorArre;

    Present_setting mPresent_setting;

    private ChooseBean currentSelectProvince;
    private ChooseBean currentSelectCity;
    private ChooseBean currentSelectHospital;
    private ChooseBean currentSelectRoom;
    private ChooseBean currentSelectDoctor;
    private Datapick currentSelectDate;
    private Patient currentSelectPatient;


    private boolean isLoadingHistoryData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {

        btn_save = (Button) findViewById(R.id.setting_btn_save);
        btn_changeUser = (Button) findViewById(R.id.setting_btn_changeuser);

        btn_save.setOnClickListener(this);
        btn_changeUser.setOnClickListener(this);


        mPresent_setting = new Present_setting(this);

        ll_province = (LinearLayout) findViewById(R.id.setting_ll_province);
        ll_province.setOnClickListener(this);
        ll_city = (LinearLayout) findViewById(R.id.setting_ll_city);
        ll_city.setOnClickListener(this);
        ll_hospital = (LinearLayout) findViewById(R.id.setting_ll_hospital);
        ll_hospital.setOnClickListener(this);
        ll_room = (LinearLayout) findViewById(R.id.setting_ll_room);
        ll_room.setOnClickListener(this);
        ll_patient = (LinearLayout)findViewById(R.id.setting_ll_user);
        ll_patient.setOnClickListener(this);

        ll_doctor = (LinearLayout) findViewById(R.id.setting_ll_doctor);
        ll_doctor.setOnClickListener(this);
        ll_orderTyle = (LinearLayout) findViewById(R.id.setting_ll_ordertype);
        ll_orderTyle.setOnClickListener(this);

        tv_city = (TextView) findViewById(R.id.setting_tv_city);
        tv_province = (TextView) findViewById(R.id.setting_tv_province);
        tv_hospital = (TextView) findViewById(R.id.setting_tv_hospital);
        tv_room = (TextView) findViewById(R.id.setting_tv_room);
        tv_doctor = (TextView) findViewById(R.id.setting_tv_doctor);
        tv_orderType = (TextView) findViewById(R.id.setting_tv_ordertype);
        tv_patient = (TextView) findViewById(R.id.setting_tv_user);

        ll_hospitalArea = (LinearLayout) findViewById(R.id.setting_ll_areahospital);
        ll_roomArea = (LinearLayout) findViewById(R.id.setting_ll_roomarea);
        ll_doctorArre = (LinearLayout) findViewById(R.id.setting_ll_doctorarea);


        if(getsp().getBoolean("isFirstUse",true)){
            mPresent_setting.setProvinceList();
            mPresent_setting.setPatient("",((App) getApplication()).getCookies());
        }else{
            isLoadingHistoryData = true;
            loadHistoryData();
        }
    }



    @Override
    public void showProcessDialog() {
        super.showProgressDialog();
    }

    @Override
    public void dismisProcessDialog() {
        closeProcessDialog(1);
    }

    @Override
    public void setProvinces(ArrayList<ChooseBean> list) {
        this.provincesList = list;
        if(isLoadingHistoryData&&currentSelectProvince!=null&&list!=null){
            if(!Md5.isContantInList(list,currentSelectProvince.get_id())){
                currentSelectProvince = null;
                mPresent_setting.setPatient("",((App)getApplication()).getCookies());
            }else{
                tv_province.setText(currentSelectProvince.getName());
                mPresent_setting.setCityList(currentSelectProvince.get_id());
            }
        }
    }



    @Override
    public void setCitys(ArrayList<ChooseBean> list) {
        this.cityList = list;

        if(isLoadingHistoryData&&list!=null){
            loadCity();
            if(Md5.isContantInList(list,currentSelectCity.get_id())){
               tv_city.setText(currentSelectCity.getName());
                mPresent_setting.setHospitalList(currentSelectProvince.get_id(), currentSelectCity.get_id());
            }else{
                currentSelectCity = null;
                mPresent_setting.setPatient("",((App)getApplication()).getCookies());
            }
        }

    }

    @Override
    public void setHospital(ArrayList<ChooseBean> list) {
        this.hospitalList = list;
        if(isLoadingHistoryData&&list!=null){
            loadHospital();
            if(Md5.isContantInList(list,currentSelectHospital.get_id())){
                tv_hospital.setText(currentSelectHospital.getName());
                mPresent_setting.setRoomList(currentSelectHospital.get_id());
            }else{
                currentSelectHospital = null;
                mPresent_setting.setPatient("",((App)getApplication()).getCookies());
            }
        }

    }



    @Override
    public void setRoomType(ArrayList<ChooseBean> list) {
        this.roomTypeList = list;

        if(isLoadingHistoryData&&list!=null){
            loadRoom();
            if(Md5.isContantInList(list,currentSelectRoom.get_id())){
                tv_room.setText(currentSelectRoom.getName());
                mPresent_setting.setDoctorList(currentSelectRoom.get_id(), ((App) getApplication()).getCookies());
            }else{
                currentSelectRoom = null;
                mPresent_setting.setPatient("",((App)getApplication()).getCookies());
            }
        }


    }

    @Override
    public void setDoctor(ArrayList<ChooseBean> list) {
        this.doctorList = list;
        if(isLoadingHistoryData&&list!=null){
            loadDoctor();
            if(Md5.isContantInList(list,currentSelectDoctor.get_id())){
                mPresent_setting.setDateList(currentSelectDoctor.get_id(), ((App) getApplication()).getCookies());
            }else{
                currentSelectDoctor = null;
                mPresent_setting.setPatient("",((App)getApplication()).getCookies());
            }
        }

    }



    @Override
    public void setDate(ArrayList<Datapick> list) {
        if(list.size()>=1){
            if(list.get(0).getUrl().equals("")){
                retryLogin(1);
                return;
            }
        }
        this.datelist = list;

        if(isLoadingHistoryData){
            loadDate();
            if(compareDate()&&compareUrl()){
                tv_doctor.setText(currentSelectDoctor.getName()+" "+currentSelectDate.getDate()+" "+currentSelectDate.getAmorPm());
            }
            mPresent_setting.setPatient("",((App)getApplication()).getCookies());
        }else {
            showsChedules();
        }
    }



    private boolean compareUrl() {

        for(int i = 0;i<datelist.size();i++){
            if(datelist.get(i).getUrl().equals(currentSelectDate.getUrl())){
                return true;
            }
        }
        return false;
    }

    private boolean compareDate() {
        String selectData = currentSelectDate.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(System.currentTimeMillis());
        try {
            sdf.parse(selectData);
            Calendar selectTime =  sdf.getCalendar();
            if(currentTime.compareTo(selectTime)==-1){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return false;
    }

    @Override
    public void setPatiner(ArrayList<Patient> list) {
        if(list == null){
            retryLogin(2);
        }
        this.patientsList = list;
        if(isLoadingHistoryData){
            loadPatiner();
            if(patinetIsInlist()){
                tv_patient.setText(currentSelectPatient.getName());
            }
            isLoadingHistoryData = false;
        }


    }


    private boolean patinetIsInlist() {
        if(patientsList==null){
            return false;
        }

        for(int i = 0 ; i < patientsList.size();i++){
            if(patientsList.get(i).getName().equals(currentSelectPatient.getName())){
                return true;
            }
        }
        return false;
    }

    private void retryLogin(final int flag) {
        final App app = (App)getApplication();
        final String userName = app.getUserName();
        final String pwd = app.getPwd();
        Log.i("retryLogin", app.getCookies());
        class MyretryLogin extends AsyncTask<String,String,String>{

            @Override
            protected void onPostExecute(String s) {
                if(s.equals("unknow_error")){
                    showToast("服务器数据异常，请稍后再试");
                }else{
                    if(flag==1)
                        mPresent_setting.setDateList(currentSelectDoctor.get_id(),app.getCookies());
                    if(flag==2)
                        mPresent_setting.setPatient("",((App)getApplication()).getCookies());
                }
                dismisProcessDialog();
                super.onPostExecute(s);
            }

            @Override
            protected void onPreExecute() {
                Setting.this.showProgressDialog();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String logOutUrl = "http://www.guahao.com/user/logout";
                try {
                    Md5.doGet(logOutUrl,"utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                model_Login model_login = new model_Login(Setting.this);
                String result = "";
                int time = 0;
                while (!(result.equals("LOGIN_SUCCESS")||result.equals("NO_NETWORK")||time>=5)){
                    result = model_login.model_Login(userName,pwd);
                    time++;
                }

                if(result.equals("LOGIN_SUCCESS")){
                    app.setCookies(model_login.getSessionId());
                    return "retry login success";
                }else{
                    return "unknow_error";
                }

            }

        }
        new MyretryLogin().execute();
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.setting_ll_room){
            chooseRoom();
        }else if(id == R.id.setting_ll_city){
            chooseCitys();
        }else if(id == R.id.setting_ll_hospital){
            chooseHospital();
        }else if (id == R.id.setting_ll_province){
            chooseProvinces();
        }else if(id == R.id.setting_ll_doctor){
            chooseDoctor();
        }else if(id == R.id.setting_ll_user){
            choosePatient();
        }else if(id == R.id.setting_btn_save){
            saveDate();

        }else if(id == R.id.setting_btn_changeuser){
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
            this.finish();
        }
    }



    private void initDialog(final ArrayList<ChooseBean> list,final int Type){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.simplelistview,null);
        builder.setTitle("请选择");
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        ListView mListView =(ListView) view.findViewById(R.id.simple_listview);
        ChooseAdapter ca = new ChooseAdapter(list,this);

        mListView.setAdapter(ca);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if(Type==1){
                    currentSelectProvince = list.get(position);
                    tv_province.setText(list.get(position).getName());
                    mPresent_setting.setCityList(currentSelectProvince.get_id());
                    currentSelectCity = new ChooseBean("all","不限",2);
                    mPresent_setting.setHospitalList(currentSelectProvince.get_id(), currentSelectCity.get_id());
                    tv_city.setText("不限");
                    tv_hospital.setText("请选择");
                    tv_room.setText("请选择");
                    currentSelectHospital=null;
                    currentSelectRoom = null;
                    currentSelectDoctor = null;
                    tv_doctor.setText("请选择");

                }else if(Type ==2){
                    currentSelectCity = list.get(position);
                    tv_city.setText(list.get(position).getName());
                    mPresent_setting.setHospitalList(currentSelectProvince.get_id(),currentSelectCity.get_id());
                    tv_hospital.setText("请选择");
                    currentSelectHospital=null;
                    tv_room.setText("请选择");
                    currentSelectRoom = null;
                    currentSelectDoctor = null;
                    tv_doctor.setText("请选择");
                }else if(Type ==3){
                    currentSelectHospital = list.get(position);
                    tv_hospital.setText(list.get(position).getName());
                    mPresent_setting.setRoomList(currentSelectHospital.get_id());
                    tv_room.setText("请选择");
                    currentSelectRoom = null;
                    currentSelectDoctor = null;
                    tv_doctor.setText("请选择");
                }else if(Type==4){
                    currentSelectRoom = list.get(position);
                    tv_room.setText(list.get(position).getName());
                    mPresent_setting.setDoctorList(currentSelectRoom.get_id(),((App)getApplication()).getCookies());
                    currentSelectDoctor = null;
                    tv_doctor.setText("请选择");
                }else if(Type==5){
                    currentSelectDoctor = list.get(position);
                    mPresent_setting.setDateList(currentSelectDoctor.get_id(),((App)getApplication()).getCookies());
                }
            }
        });
        dialog.show();
    }

//    private void  initDialog(ArrayList<Doctor> list){
//
//    }


    private void showsChedules(){
        if(currentSelectDoctor == null){
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view  = LayoutInflater.from(this).inflate(R.layout.simplelistview,null);

        builder.setView(view);
        builder.setTitle("请选择");
        final AlertDialog dialog = builder.create();
        dialog.show();

        ListView listView = (ListView)view.findViewById(R.id.simple_listview);
        DatePickAdapter adapter = new DatePickAdapter(datelist,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentSelectDate = datelist.get(position);
                tv_doctor.setText(currentSelectDoctor.getName() + " " + currentSelectDate.getDate() + " " + currentSelectDate.getAmorPm());
                dialog.dismiss();
            }
        });
    }

    private void chooseProvinces() {

        if(provincesList==null){
            return;
        }
        initDialog(this.provincesList, 1);
    }

    private void chooseCitys() {
        if(currentSelectProvince==null||cityList==null){
            return;
        }
        initDialog(this.cityList,2);
    }

    private void chooseHospital() {
        if(currentSelectProvince==null||currentSelectCity==null||hospitalList==null){
            return;
        }
        initDialog(this.hospitalList,3);

    }
    private void chooseRoom() {
        if(currentSelectHospital==null||roomTypeList==null){
            return;
        }

        initDialog(this.roomTypeList, 4);
    }
    private void chooseDoctor() {
        if(currentSelectRoom==null||doctorList==null){
            return;
        }
        initDialog(doctorList, 5);
    }

    private void choosePatient() {
        if(patientsList==null){
            return;
        }
        initPatientDialog();
    }

    private void initPatientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        View view = LayoutInflater.from(this).inflate(R.layout.simplelistview, null);
        builder.setTitle("请选择");
        builder.setView(view);
        final Dialog dialog = builder.create();


        ListView listView = (ListView) view.findViewById(R.id.simple_listview);
        PatientAdapter adapter = new PatientAdapter(this,patientsList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentSelectPatient = patientsList.get(position);
                tv_patient.setText(currentSelectPatient.getName());
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void saveDate() {
        if(currentSelectDate==null||currentSelectProvince==null||currentSelectCity==null
                ||currentSelectHospital==null||currentSelectRoom==null||currentSelectDoctor==null||currentSelectPatient==null){
            showToast("请输入完整信息");
            return;
        }

        if(Md5.checkServiceisRunning(this)){
            showServiceRunningDialog();
            return;
        }

        if(currentSelectDate.getState()!=4){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("确定?");
            builder.setMessage("发现您选择的日期为非可预约状态，app会在后台不断尝试预约直到预约成功或者您主动点击取消预约，点击确定确定预约，点击取消重新选择日期");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sava();
                    Setting.this.finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            sava();
            Setting.this.finish();
        }

    }

    private void showServiceRunningDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定?");
        builder.setMessage("发现您已经在尝试预约，是否取消当前预约，保存此预约");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopCurrentService();
                saveDate();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void stopCurrentService() {
        Intent i = new Intent(this, OrderService.class);
        stopService(i);
    }

    private void sava(){
        saveHospital();
        saveProvince();
        saveCity();
        saveRoom();
        saveDoctor();
        saveData();
        savePatient();
        getsp().edit().putBoolean("isFirstUse",false).commit();
        showToast("保存成功");
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    private void savePatient() {
        SharedPreferences sp = getsp();
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("patientName", currentSelectPatient.getName());
        ed.putString("patientIdCard", currentSelectPatient.getIdCard());
        ed.putString("patientPhone", currentSelectPatient.getPhone());
        ed.commit();

    }

    private void saveData() {
        SharedPreferences sp = getsp();
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("dateUrl",currentSelectDate.getUrl());
        ed.putInt("dateType", currentSelectDate.getType());
        ed.putInt("dateState", currentSelectDate.getState());
        ed.putString("dateClinicType", currentSelectDate.getClinicType());
        ed.putString("dateAmOrPm", currentSelectDate.getAmorPm());
        ed.putString("dateDate",currentSelectDate.getDate());
        ed.commit();
    }

    private void saveDoctor() {
        SharedPreferences sp = getsp();
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("doctorId", currentSelectDoctor.get_id());
        ed.putString("doctorName", currentSelectDoctor.getName());
        ed.commit();
    }

    private void saveRoom() {
        SharedPreferences sp = getsp();
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("roomId", currentSelectRoom.get_id());
        ed.putString("roomName", currentSelectRoom.getName());
        ed.commit();
    }

    private void saveCity() {
        SharedPreferences sp = getsp();
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("cityId", currentSelectCity.get_id());
        ed.putString("cityName", currentSelectCity.getName());
        ed.commit();
    }

    private void saveProvince() {
        SharedPreferences sp = getsp();
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("provinceId", currentSelectProvince.get_id());
        ed.putString("provinceName", currentSelectProvince.getName());
        ed.commit();
    }

    private void saveHospital() {
        SharedPreferences sp = getsp();
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("hospitalId",currentSelectHospital.get_id());
        ed.putString("hospitalName", currentSelectHospital.getName());
        ed.commit();
    }

    private SharedPreferences sp;
    private SharedPreferences getsp(){
        if(sp == null){
            String name = ((App)getApplication()).getUserName();
            sp = getSharedPreferences(name, MODE_PRIVATE);
        }
        return sp;
    }

    private void loadHistoryData() {
        loadProvince();
    }


    private void loadProvince() {
        SharedPreferences sp = getsp();
        String provincelName = sp.getString("provinceName", "请选择");
        String provinceId = sp.getString("provinceId","-1");
        currentSelectProvince = new ChooseBean(provinceId,provincelName,1);
        mPresent_setting.setProvinceList();
    }

    private void loadCity() {
        SharedPreferences sp =getsp();
        String cityId = sp.getString("cityId", "-1");
        String cityName = sp.getString("cityName","不限");
        currentSelectCity = new ChooseBean(cityId,cityName,2);
    }
    private void loadHospital() {
        SharedPreferences sp = getsp();
        String hospitalId = sp.getString("hospitalId", "-1");
        String hospitalName = sp.getString("hospitalName","");
        currentSelectHospital = new ChooseBean(hospitalId,hospitalName,3);
    }

    private void loadRoom() {
        SharedPreferences sp = getsp();
        String roomId = sp.getString("roomId", "-1");
        String roomName = sp.getString("roomName","");
        currentSelectRoom = new ChooseBean(roomId,roomName,4);
    }
    private void loadDoctor() {
        SharedPreferences sp = getsp();
        String doctorId = sp.getString("doctorId", "-1");
        String doctorName = sp.getString("doctorName","");
        currentSelectDoctor = new ChooseBean(doctorId,doctorName,5);
    }

    private void loadDate() {
        SharedPreferences sp = getsp();
        String dateUrl = sp.getString("dateUrl", "");
        String dateCinicType = sp.getString("dateClinicType","");
        String dateAmOrPm = sp.getString("dateAmOrPm","");
        String dateDate = sp.getString("dateDate","");
        int type = sp.getInt("dateType", -1);
        int dateState = sp.getInt("dateState",-1);

        currentSelectDate = new Datapick();
        currentSelectDate.setUrl(dateUrl);
        currentSelectDate.setClinicType(dateCinicType);
        currentSelectDate.setAmorPm(dateAmOrPm);
        currentSelectDate.setDate(dateDate);
        currentSelectDate.setType(type);
        currentSelectDate.setState(dateState);
    }


    private void loadPatiner() {
        SharedPreferences sp = getsp();
        String name = sp.getString("patientName", "");
        String idCard = sp.getString("patientIdCard","");
        String phone = sp.getString("patientPhone","");
        currentSelectPatient = new Patient();
        currentSelectPatient.setName(name);
        currentSelectPatient.setIdCard(idCard);
        currentSelectPatient.setPhone(phone);
    }

}
