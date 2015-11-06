package edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.cqut.cn.guahaoapp.edu.bean.ChooseBean;
import edu.cqut.cn.guahaoapp.R;

/**
 * Created by dun on 2015/10/17.
 */
public class ChooseAdapter extends BaseAdapter {
    ArrayList<ChooseBean> list;
    Context mContext;
    public ChooseAdapter (ArrayList<ChooseBean> list,Context mContext){
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
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_listview_item,null);
            holder = new ViewHolder();
            holder.textView =(TextView) convertView.findViewById(R.id.simple_listview_item_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

            holder.textView.setText(list.get(position).getName());

        return convertView;
    }

    private class ViewHolder{
        TextView textView;
    }


}
