package edu.cqut.cn.guahaoapp.edu.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.cqut.cn.guahaoapp.MainActivity;
import edu.cqut.cn.guahaoapp.R;
import edu.cqut.cn.guahaoapp.edu.App.App;
import edu.cqut.cn.guahaoapp.edu.bean.Datapick;
import edu.cqut.cn.guahaoapp.edu.bean.Patient;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.Units.Md5;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.model.model_Login;

/**
 * Created by dun on 2015/10/23.
 */
public class OrderService extends Service{

    public static String OrderServiceActionStart= "edu.cqut.cn.guahaoapp.edu.service.OrderService.OrderServiceActionStart";
    public static String OrderServiceActionStop= "edu.cqut.cn.guahaoapp.edu.service.OrderService.OrderServiceActionStop";

    private String userName = "";
    private String pwd = "";
    private String doctorId = "";
    private String orderUrl = "";
    private String date = "";
    private String isAmOrPm = "";
    private String cookie = "";
    private int currentState = -1;
    private String patient = "";
    private int waitTime = 300000;

    private boolean isNeedRun = true;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            start(intent);
            return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        calcel();
        super.onDestroy();
    }

    private void calcel() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFY_FLAG);
        isNeedRun = false;
    }

    private void start(Intent intent) {
        getDate(intent);
        checkDoctorState();
    }

    private void checkDoctorState() {
        class MycheckDoctorState extends AsyncTask<String,String,String>{

            Datapick datapick;

            @Override
            protected void onPostExecute(String s) {
                checkDoctorState(datapick);
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                do{
                    datapick = getDoctorDate();
                    if(datapick.getState()!=4){
                        try {
                            Log.i("service","不可预约状态");
                            Thread.sleep(waitTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }while(datapick.getState()!=4);

                return null;
            }
        }
        new MycheckDoctorState().execute();
    }

    private void checkDoctorState(Datapick datapick) {
        if(datapick == null){
            retryLogin();
            checkDoctorState();
            return;
        }

        if(datapick.getState()==4){
            StartOrder(datapick);
        }
    }

    private void StartOrder(final Datapick datapick) {
        class MyStartOrkerAsync extends AsyncTask<String,String,String>{

            @Override
            protected void onPostExecute(String s) {
                if(s.equals(ORDER_RESULT_VFCODE_ERROR)){
                    retryOrder(datapick);
                }else if(s.equals(ORDER_RESULT_SUCCESS)){
                    String msg = patient+"的"+date+" "+isAmOrPm+" 的预约已经成功";
                    orderSuccess("预约成功",msg);

                    Intent i = new Intent();
                    i.setAction("orderSuccess");
                    sendBroadcast(i);

                }else if(s.equals(ORDER_RESULT_HASBEENORDER)){
                    orderSuccess("预约失败","同一个用户只能有一个订单");
                    Intent i = new Intent();
                    i.setAction("orderFail");
                    i.putExtra("reason", "同一个用户只能有一个订单");
                    sendBroadcast(i);
                }

                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                Patient p =null;
                do {
                    p = getPatient(datapick.getUrl(),cookie);
                }while(p==null);
                String VFCode = getVFcode();
                String result = Order(p, VFCode);
                return result;
            }
        }

        new MyStartOrkerAsync().execute();

    }

    private void orderSuccess(String title,String msg) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFY_FLAG);



        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        i.putExtra("isServiceRunning",false);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notify = new Notification.Builder(this).setSmallIcon(R.drawable.logo)
                .setTicker(title)
                .setContentTitle("挂号app")
                .setContentText(msg).setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent).getNotification();
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(100, notify);



        this.stopSelf();

    }

    private void retryOrder(Datapick datapick) {
        StartOrder(datapick);
    }


    private static String ORDER_RESULT_VFCODE_ERROR = "ORDER_RESULT_VFCODE_ERROR";
    private static String ORDER_RESULT_UNKNOW_ERROE = "ORDER_RESULT_UNKNOW_ERROE'";
    private static String ORDER_RESULT_SUCCESS = "ORDER_RESULT_SUCCESS'";
    private static String ORDER_RESULT_HASBEENORDER = "ORDER_RESULT_HASBEENORDER'";
    private String Order(Patient p, String vfCode) {

        String URLString = "http://www.guahao.com/my/reservation/submitvalidate";
        HashMap<String,String> map = p.getDataMap();
        map.put("RequireNameStr0","");
        map.put("visitType","0");
        map.put("RequireNameStr1","1_require_0cardType,,就诊卡类型:1_require_0cardValue,,卡号:");
        map.put("1_require_0cardType","1");
        map.put("1_require_0cardValue","");
        map.put("validCode",vfCode);
        map.put("knowit","on");
        map.put("diseaseName","123");
        map.put("patient.encodePatientId",p.getId());
        try {
            Document doc = Jsoup.connect(URLString)
                    .header("Cookie", cookie)
                    .data(map)
                    .followRedirects(true)
                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.7.1000 Chrome/30.0.1599.101 Safari/537.36")
                    .header("Connection","keep-alive")
                    .post();

            Element e = doc.getElementById("g-cfg");
            Elements elements =  e.getElementsByTag("p");
            for(int i = 0 ; i < elements.size() ; i++){
                if(elements.get(i).text().equals("图片验证码填写错误，请重新填写")){
                    Log.i("Service","验证码错误");
                    return ORDER_RESULT_VFCODE_ERROR;
                }else if(elements.get(i).text().equals("同一个用户在同一个班次只能有一个订单！")||elements.get(i).text().equals("同一患者同医院同科室只能预约一笔有效订单，请就诊完毕或取消订单后再尝试预约。")){
                    return ORDER_RESULT_HASBEENORDER;
                }
            }
            elements = e.getElementsByTag("h2");
            for(int i = 0 ; i <elements.size() ; i++){
                if(elements.get(i).text().equals("预约成功！")){
                    Log.i("Service","预约成功");
                    return ORDER_RESULT_SUCCESS;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return ORDER_RESULT_UNKNOW_ERROE;
    }

    private String getVFcode() {
        String result = "";

        InputStream is = null;
        String sdCardPath = Environment.getExternalStorageDirectory().getPath()+"/GUAHAOAPP";

        File file = new File(sdCardPath);
        if(!file.exists()){
            file.mkdirs();
        }


        File imageFile = new File(sdCardPath,"image.jpeg");


        URL url = null;
        try {
            url = new URL("http://www.guahao.com/validcode/genimage/1.jpeg");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.7.1000 Chrome/30.0.1599.101 Safari/537.36");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Language", "zh-cn");
            connection.setRequestProperty("Cache-Control", "no-cache");

            connection.setDoOutput(true);
            connection.setDoInput(true);



            connection.connect();
            is= connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);



            out.flush();
            out.close();
            is.close();
            result = uploadForm(new HashMap<String, String>(), "image", imageFile, "image"+ ".jpeg", "http://120.25.251.45:8084/Home/Results");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";

    /**
     *
     * @param params
     *            传递的普通参数
     * @param uploadFile
     *            需要上传的文件名
     * @param fileFormName
     *            需要上传文件表单中的名字
     * @param newFileName
     *            上传的文件名称，不填写将为uploadFile的名称
     * @param urlStr
     *            上传的服务器的路径
     * @throws IOException
     */
    public String uploadForm(Map<String, String> params, String fileFormName,
                             File uploadFile, String newFileName, String urlStr)
            throws IOException {
        String s ="";
        if (newFileName == null || newFileName.trim().equals("")) {
            newFileName = uploadFile.getName();
        }

        StringBuilder sb = new StringBuilder();
        /**
         * 普通的表单数据
         */
        for (String key : params.keySet()) {
            sb.append("--" + BOUNDARY + "\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + key + "\""
                    + "\r\n");
            sb.append("\r\n");
            sb.append(params.get(key) + "\r\n");
        }
        /**
         * 上传文件的头
         */
        sb.append("--" + BOUNDARY + "\r\n");
        sb.append("Content-Disposition: form-data; name=\"" + fileFormName
                + "\"; filename=\"" + newFileName + "\"" + "\r\n");
        sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
        sb.append("\r\n");

        byte[] headerInfo = sb.toString().getBytes("UTF-8");
        byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");


        Log.i("head", sb.toString());


        URL url = new URL(urlStr);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);
        conn.setRequestProperty(
                "Content-Length",
                String.valueOf(headerInfo.length + uploadFile.length()
                        + endInfo.length));
        conn.setDoOutput(true);
        conn.setDoInput(true);

        OutputStream out = conn.getOutputStream();
        InputStream in = new FileInputStream(uploadFile);
        out.write(headerInfo);



        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1)
            out.write(buf, 0, len);

        out.write(endInfo);
        out.flush();
        in.close();
        out.close();
        if (conn.getResponseCode() == 200||conn.getResponseCode()==302) {
            System.out.println("上传成功");
            InputStream is = conn.getInputStream();

            int b;

            while ((b = is.read()) != -1){
                byte[] buffer = new byte[1024];
                is.read(buffer);
                s+=new String(buffer,"utf-8");

            }

        }
        int resultCode = conn.getResponseCode();
        Log.i("resultCode",resultCode+"");

        if(resultCode==200){
            return dealWithVfcode200(s);
        }else if(resultCode==302){
            return dealWithVfcode302(s);
        }


        return  s;
    }

    private String dealWithVfcode302(String s) {
        String result;

        Document doc = Jsoup.parse(s);
        Elements elements = doc.getElementsByTag("a");
        String href = elements.get(0).attr("href");

        String[] arr = href.split("=");
        result = arr[arr.length-1];

        return result;

    }

    private String dealWithVfcode200(String result) {
        Document doc = Jsoup.parse(result);
        Elements elements =  doc.getElementsByClass("well");
        Elements es = elements.get(0).getElementsByTag("h1");
        String Vfcoderesult = es.get(0).html();
        Log.i("vfCode",Vfcoderesult);
        return Vfcoderesult;
    }

    private Patient getPatient(String url,String cookieI) {
        Patient p = null;
        String urlString = "http://www.guahao.com"+url;
        Log.i("getPatientUrl",urlString);

        try {
            Connection.Response response=  Jsoup.connect(urlString)
                    .header("Cookie", cookie).execute();

            Map<String,String> map = response.cookies();

            cookie += "__rf__="+map.get("__rf__")+";";


            Document doc = Jsoup.parse(response.body());
            HashMap<String,String> dateMap = new HashMap<>();
            Element baseElement = doc.getElementById("J_Form");
            Elements elements =  baseElement.getElementsByAttributeValue("type","hidden");

            for(int i = 0 ; i<elements.size();i++){
                Element e = elements.get(i);
                String name =  e.attr("name");
                String value = e.attr("value");
                dateMap.put(name,value);
            }

            Elements patientsBox = doc.getElementsByClass("tool_id");
            for(int i = 0;i<patientsBox.size();i++){
                Element e = patientsBox.get(i);

                String name = e.getElementsByTag("dt").text().split(" ")[0];

                if(!name.equals(patient)){
                    continue;
                }
                String id = e.attr("data-id");
                String phone_idCard = e.getElementsByTag("dd").text();
                String icCard = phone_idCard.split(" ")[0];
                String phone = phone_idCard.split(" ")[1];
                p = new Patient();
                p.setDataMap(dateMap);
                p.setId(id);
                p.setName(name);
                p.setIdCard(icCard);
                p.setPhone(phone);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return p;

    }


    private void retryLogin() {
        class MyretryLogin extends AsyncTask<String,String,String>{

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }


            @Override
            protected String doInBackground(String... params) {
                String logOutUrl = "http://www.guahao.com/user/logout";
                try {
                    Md5.doGet(logOutUrl,"utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                model_Login model_login = new model_Login(OrderService.this);
                String result = "";
                while (!(result.equals("LOGIN_SUCCESS"))){
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    result = model_login.model_Login(userName,pwd);
                }



                   cookie = model_login.getSessionId();
                    return "retry login success";
            }

        }
        new MyretryLogin().execute();

    }

    private Datapick getDoctorDate() {
        Datapick datapick = null;

        HashMap<String,String> map = new HashMap<>();
        map.put("expertId",doctorId);
        String url = "http://www.guahao.com/expert/shiftcase/";
        try {
            String result = Md5.doGet(url, map, "utf-8", cookie);
            ArrayList<Datapick> list = dealWithJsonDate(result);
            datapick = getCurrentDatapick(list);

        } catch (Exception e) {
            e.printStackTrace();
        }




        return datapick;

    }

    private Datapick getCurrentDatapick(ArrayList<Datapick> list) {
        if(list.size()>0){
            if(list.get(0).getUrl().equals("")){
                return null;
            }
        }

        for(int i = 0 ; i <list.size(); i++){
            Datapick d = list.get(i);
            if(d.getDate().equals(date)&&d.getAmorPm().equals(isAmOrPm)){
                return d;
            }
        }


        return new Datapick();
    }

    private ArrayList<Datapick> dealWithJsonDate(String s) {
        Log.i("date", s);
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

        return list;

    }


    private void getDate(Intent intent) {
        Bundle b = intent.getExtras();
        userName = b.getString("userName");
        pwd = b.getString("pwd");
        cookie = b.getString("cookie");
        doctorId = b.getString("doctorId");
        orderUrl = b.getString("orderUrl");
        date = b.getString("date");
        isAmOrPm = b.getString("isAmOrPm");
        currentState = b.getInt("currentState");
        patient = b.getString("patient");
    }


    private  int StartOrder = 0x10001;
    private  int CancelOrder = 0x10002;






    private int NOTIFY_FLAG = 0x10000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static class MyHandler extends  Handler{
        private  int StartOrder = 0x10001;
        private  int CancelOrder = 0x10002;
        private  int OrderError = 0x10003;
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            int what = msg.what;
            if(what==StartOrder){
                Log.i("service","正在开始预约....");
            }else if(what == CancelOrder){
                Log.i("service","取消了预约");
            }else if(what == OrderError){
                Log.i("service","预约失败");
            }
            super.dispatchMessage(msg);

        }
    }
}
