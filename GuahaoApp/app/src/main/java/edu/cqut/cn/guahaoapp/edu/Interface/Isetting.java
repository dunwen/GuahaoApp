package edu.cqut.cn.guahaoapp.edu.Interface;

import java.util.ArrayList;

import edu.cqut.cn.guahaoapp.edu.bean.ChooseBean;
import edu.cqut.cn.guahaoapp.edu.bean.Datapick;
import edu.cqut.cn.guahaoapp.edu.bean.Patient;

/**
 * Created by dun on 2015/10/17.
 */
public interface Isetting {
    public void showProcessDialog();
    public void dismisProcessDialog();

    public void setProvinces(ArrayList<ChooseBean> list);
    public void setCitys(ArrayList<ChooseBean> list);
    public void setHospital(ArrayList<ChooseBean> list);
    public void setRoomType(ArrayList<ChooseBean> list);
    public void setDoctor(ArrayList<ChooseBean> list);
    public void setDate(ArrayList<Datapick> list);
    public void setPatiner(ArrayList<Patient> list);
    public void showToast(String msg);
}
