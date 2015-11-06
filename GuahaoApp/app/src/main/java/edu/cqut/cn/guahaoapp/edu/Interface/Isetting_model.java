package edu.cqut.cn.guahaoapp.edu.Interface;

import java.util.ArrayList;

import edu.cqut.cn.guahaoapp.edu.bean.ChooseBean;
import edu.cqut.cn.guahaoapp.edu.bean.Patient;

/**
 * Created by dun on 2015/10/17.
 */
public interface Isetting_model {
    String getProvince();
    String getcity(String provinceId);
    String getHospital(String hospitalId,String cityId);
    String getRoom(String hospitalId);
    String getDate(String doctorId,String cookie);
    ArrayList<Patient> getPatient(String url,String cookie);
    ArrayList<ChooseBean> getDoctor(String roomId,String cookie);
}
