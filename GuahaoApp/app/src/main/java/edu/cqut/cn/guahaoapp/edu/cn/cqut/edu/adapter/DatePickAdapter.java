package edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.cqut.cn.guahaoapp.edu.bean.Datapick;
import edu.cqut.cn.guahaoapp.R;

/**
 * Created by dun on 2015/10/19.
 */
public class DatePickAdapter extends BaseAdapter{

    ArrayList<Datapick> list;
    Context mContext;

    public DatePickAdapter(ArrayList<Datapick> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dataselsct_item,null);
            holder = new ViewHolder();
            holder.date = (TextView)convertView.findViewById(R.id.dataselect_item_data);
            holder.stauts = (TextView)convertView.findViewById(R.id.dataselect_item_state);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
            Datapick d = list.get(position);
            holder.date.setText(d.getDate()+"     "+d.getAmorPm());
            holder.stauts.setText(d.getStateString());

            int state = d.getState();
            if(state==2||state==3){
                holder.stauts.setTextColor(0xffE91616);
            }else if(state==0||state==1){
                holder.stauts.setTextColor(0xff808080);
            }else{
                holder.stauts.setTextColor(0xff00D535);
            }


        return convertView;
    }

    private class ViewHolder{
        public TextView date;
        public TextView stauts;

    }
}
