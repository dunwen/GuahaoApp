package edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cqut.cn.guahaoapp.edu.Interface.Isetting_model;
import edu.cqut.cn.guahaoapp.edu.bean.ChooseBean;
import edu.cqut.cn.guahaoapp.edu.bean.Patient;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.Units.Md5;

/**
 * Created by dun on 2015/10/17.
 */
public class model_setting implements Isetting_model{
    private String NO_NETWORK = "NO_NETWORK";

    @Override
    public String getProvince() {
        try {
            return Md5.doGet("http://www.guahao.com/json/white/area/provinces","utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "NO_NETWORK";
        }
    }



    @Override
    public String getcity(String provincelId) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("provinceId",provincelId);

        try {
            return Md5.doGet("http://www.guahao.com/json/white/area/citys",map,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return NO_NETWORK;
        }

    }

    @Override
    public String getHospital(String provinceId, String cityId) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("provinceId",provinceId);
        map.put("cityId",cityId);
        try {
            return Md5.doGet("http://www.guahao.com/json/white/fastorder/hospitals",map,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return NO_NETWORK;
        }
    }

    @Override
    public String getRoom(String hospitalId) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("hospitalId",hospitalId);
        try {
            return Md5.doGet("http://www.guahao.com/json/white/fastorder/depts",map,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return  NO_NETWORK;
        }
    }

    @Override
    public String getDate(String doctorId,String cookie) {
        HashMap<String,String> map = new HashMap<>();
        map.put("expertId",doctorId);
        String url = "http://www.guahao.com/expert/shiftcase/";
        try {
            return Md5.doGet(url,map,"utf-8",cookie);
        } catch (Exception e) {
            e.printStackTrace();
            return NO_NETWORK;
        }
    }



    @Override
    public ArrayList<ChooseBean> getDoctor(String roomId,String cookie) {
        ArrayList<ChooseBean> list = new ArrayList<ChooseBean>();
        String urlString = "http://www.guahao.com/department/shiftcase/"+roomId;


        try {
            Document doc = Jsoup.connect(urlString).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.7.1000 Chrome/30.0.1599.101 Safari/537.36")

                    .get();

            Elements elements = doc.getElementsByClass("doc-base-info");

            for(int  i = 0 ; i <elements.size();i++){
                Element e = elements.get(i);
                Elements doctorInfoElement = e.getElementsByClass("name");
                String name = doctorInfoElement.get(0).html();
                String baseUri = doctorInfoElement.get(0).attr("href");
                String[] tempUrl = baseUri.split("\\?");
                String[] temp = tempUrl[0].split("/");
                String id = temp[temp.length-1];
                ChooseBean bean = new ChooseBean();
                bean.setType(5);
                bean.setName(name);
                bean.set_id(id);
                list.add(bean);
            }



        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return list;
    }

    @Override
    public ArrayList<Patient> getPatient(String url, String cookie) {
        ArrayList<Patient> list = new ArrayList<>();

        String urlString = "http://www.guahao.com/my/patients";
        try {
            Document doc = Jsoup.connect(urlString)
                        .header("Cookie", cookie)
                        .get();

            Element element = doc.getElementById("patientListDv");
            Elements trs = element.getElementsByTag("tbody").get(0).getElementsByTag("tr");

            for(int i = 0;i<trs.size();i++){
                Elements elements = trs.get(i).getElementsByTag("td");
                Patient p = new Patient();
                p.setName(elements.get(0).text());
                p.setSex(elements.get(1).text());
                p.setAge(elements.get(2).text());
                p.setIdCard(elements.get(3).text());
                p.setPhone(elements.get(4).text());
                list.add(p);
            }



        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return list;
    }



}
