package edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.cqut.cn.guahaoapp.edu.bean.Patient;
import edu.cqut.cn.guahaoapp.R;

/**
 * Created by dun on 2015/10/21.
 */
public class PatientAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Patient> list;

    public PatientAdapter(Context mContext, ArrayList<Patient> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.patient_item,null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.patient_name);
            holder.tv_idCard = (TextView) convertView.findViewById(R.id.patient_idCard_phone);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
            Patient p = list.get(position);
            String s =p.getName();
            holder.tv_name.setText(p.getName());
            holder.tv_idCard.setText(p.getIdCard() + "\n电话号码" + p.getPhone());
        return convertView;
    }

    private class ViewHolder{
        TextView tv_name;
        TextView tv_idCard;
    }
}
