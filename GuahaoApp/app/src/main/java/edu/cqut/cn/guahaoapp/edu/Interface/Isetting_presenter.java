package edu.cqut.cn.guahaoapp.edu.Interface;

/**
 * Created by dun on 2015/10/17.
 */
public interface Isetting_presenter {
    void setProvinceList();
    void setCityList(String hospitalId);
    void setHospitalList(String provinceId,String cityId);
    void setRoomList(String hospitalId);
    void setDoctorList(String roomId,String cookie);
    void setorderType();
    void setDateList(String doctorId,String cookie);
    void setPatient(String url,String cookie);
}
