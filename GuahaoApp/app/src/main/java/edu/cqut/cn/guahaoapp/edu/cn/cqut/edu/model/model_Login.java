package edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cqut.cn.guahaoapp.edu.Interface.Ilogin_model;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.Units.Md5;

/**
 * Created by dun on 2015/10/13.
 */
public class model_Login implements Ilogin_model{
    private String sessionId = "";

    private String _sh_ssid_ = "";
    private String _e_m = "";
    private String _sid_ = "";

    private String _ci_ = "";
    private String __i__= "";

    private int time = 1;

    private String cookie;
    private Context mContext;
    private static String WRONG_USERNAME_OR_PWD ="WRONG_USERNAME_OR_PWD";
    private static String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    private static String NO_NETWORK = "NO_NETWORK";
    private int loginTime = 0;

    public  model_Login (Context mContext){
        this.mContext = mContext;
    }

    @Override
    public String  model_Login(final String userName,final String pwd){
        loginTime+=1;
        String result = "";
        String vfCode = null;
        try {
            vfCode = getVFcodeImage();
            Log.d(">>>>>>>>>>", userName + " " + pwd + " " + vfCode);
            final String md5pwd = Md5.GetMD5Code(pwd);

            String urladress = "http://www.guahao.com/user/login";
            String parms = "loginId="+userName+
                    "&password="+md5pwd+"&validCode="+vfCode+"&method=dologin&target=%2f";

            result =LoginUnit(urladress,parms);
        } catch (Exception e) {
            result = NO_NETWORK;
            return result;
        }


        if(result.equals(this.WRONG_USERNAME_OR_PWD)&&loginTime==1){
            return model_Login(userName,pwd);
        }
        Log.i("cookie",cookie);
        return result;


    }


    public String LoginUnit(final String urladress,final String parmes) throws Exception {
            String responseContent="";
            String TempCookieString = "";


                    URL url = new URL(urladress);
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Cookie", cookie);
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.7.1000 Chrome/30.0.1599.101 Safari/537.36");
                    connection.setRequestProperty("Connection", "keep-alive");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Language", "zh-cn");
                    connection.setRequestProperty("Cache-Control", "no-cache");

                    connection.setDoInput(true);
                    connection.setDoOutput(true);


                    byte[] b = parmes.toString().getBytes();
                    connection.getOutputStream().write(b, 0, b.length);
                    connection.getOutputStream().flush();
                    connection.getOutputStream().close();

                    int code = connection.getResponseCode();

                    if(code == 302){

                        Map all = connection.getHeaderFields();
                        List allCookie = (List)all.get("Set-Cookie");

                        for (int i = 0; i<allCookie.size();i++){

                            String cookieString =(String)allCookie.get(i);
                            String[] cookie = cookieString.split(";");
//                            Log.i("Cookie",cookie[0]);
                            String[] cookieSplit = cookie[0].split("\"");

                            if(cookieSplit.length<2){
                                continue;
                            }else{
                                TempCookieString += cookieSplit[0]+cookieSplit[1]+";";
                            }


                        }

                        cookie += TempCookieString;


                    }else if(code == 200){
                        Log.i("Login_rror","code is "+code +"用户名或密码错误");
                        responseContent = WRONG_USERNAME_OR_PWD;
                    }
                return getresult(responseContent);


    }


    @Override
    public String getVFcodeImage() throws Exception {
        InputStream is = null;
        String sdCardPath = Environment.getExternalStorageDirectory().getPath()+"/GUAHAOAPP";

        File file = new File(sdCardPath);
        if(!file.exists()){
            file.mkdirs();
        }


        File imageFile = new File(sdCardPath,"image.jpeg");




            URL url = new URL("http://www.guahao.com/user/login");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.7.1000 Chrome/30.0.1599.101 Safari/537.36");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Language", "zh-cn");
            connection.setRequestProperty("Cache-Control", "no-cache");
            Map all = connection.getHeaderFields();

            List allCookie = (List)all.get("Set-Cookie");

            for (int i = 0;i<allCookie.size();i++){
                String s = (String)allCookie.get(i);
                String[] allcontext = s.split(";");
                String[] temp = allcontext[0].split("=");
                if(temp[0].equals("_sh_ssid_")){
                    _sh_ssid_=temp[1];
                }else if(temp[0].equals("_e_m")){
                    _e_m = temp[1];
                }else if(temp[0].equals("_sid_")){
                    _sid_ = temp[1];
                }
            }

            cookie = "_sh_ssid_="+_sh_ssid_+";_e_m="+_e_m+";_sid_="+_sid_+";";

            url = new URL("http://www.guahao.com/validcode/genimage/1.jpeg");
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
            return uploadForm(new HashMap<String, String>(), "image", imageFile, "image" + time + ".jpeg", "http://120.25.251.45:8084/Home/Results");

    }



    @Override
    public String getSessionId() {
        return cookie;
    }

    public String getresult(String s) {
        if(s.equals(this.WRONG_USERNAME_OR_PWD)){
            return this.WRONG_USERNAME_OR_PWD;
        }else if(s.equals(this.NO_NETWORK)){
            return this.NO_NETWORK;
        }else
            return this.LOGIN_SUCCESS;
    }


    private class MyStringRequest extends StringRequest {
        private Map<String, String> mHeaders=new HashMap<String, String>(1);


        public MyStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(url, listener, errorListener);
        }

        public MyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        public void setCookies(String cookies){
            mHeaders.put("Cookie", cookies);
           mHeaders.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.7.1000 Chrome/30.0.1599.101 Safari/537.36");
            mHeaders.put("Connection","keep-alive");
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return mHeaders;
        }
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
        time++;
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
//        conn.setRequestProperty(
//                "Content-Length",
//                String.valueOf(headerInfo.length + uploadFile.length()
//                        + endInfo.length));
        conn.setConnectTimeout(8000);
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

        InputStream is = conn.getInputStream();
        if (conn.getResponseCode() == 200||conn.getResponseCode()==302) {
            System.out.println("上传成功");

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

        in.close();
        out.close();
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


}
