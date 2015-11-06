package edu.cqut.cn.guahaoapp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import edu.cqut.cn.guahaoapp.edu.App.App;
import edu.cqut.cn.guahaoapp.edu.cn.cqut.edu.Units.Md5;
import edu.cqut.cn.guahaoapp.edu.service.OrderService;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    ImageView iv_setting;
    Button btn_startOrder;
    Button btn_calcelOrder;
    MyBroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFY_FLAG);


        IntentFilter filter = new IntentFilter();
        filter.addAction("orderSuccess");
        filter.addAction("orderFail");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver,filter);
        initView();
    }


    private void initView() {
        iv_setting = (ImageView) findViewById(R.id.main_btn_setting);
        iv_setting.setOnClickListener(this);
        btn_startOrder = (Button) findViewById(R.id.main_btn_start);
        btn_startOrder.setOnClickListener(this);
        btn_calcelOrder = (Button) findViewById(R.id.btn_main_cancel);
        btn_calcelOrder.setOnClickListener(this);

        setBtn_cancel_unclickable();

        if(Md5.checkServiceisRunning(this)){
            setBtn_start_unclickable();
        }else{
            setBtn_cancel_unclickable();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.main_btn_setting){
            Intent i = new Intent(this,Setting.class);
            i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(i);
        }else if(id == R.id.main_btn_start){
            startOrderService();
            setBtn_start_unclickable();
        }else if(id == R.id.btn_main_cancel){
            stopOrderService();
            setBtn_cancel_unclickable();
        }
    }

    private void stopOrderService() {
        Intent i = new Intent(MainActivity.this,OrderService.class);
        stopService(i);
    }

    private void startOrderService() {
        Intent i = new Intent(MainActivity.this,OrderService.class);
        setData(i);
        startService(i);
    }

    private void setData(Intent i) {
        SharedPreferences sp = getSharedPreferences(((App)getApplication()).getUserName(),MODE_PRIVATE);
        i.putExtra("userName",((App)getApplication()).getUserName());
        i.putExtra("pwd", ((App) getApplication()).getPwd());
        i.putExtra("cookie", ((App) getApplication()).getCookies());
        i.putExtra("doctorId", sp.getString("doctorId", ""));
        i.putExtra("orderUrl", sp.getString("dateUrl", ""));
        i.putExtra("date", sp.getString("dateDate", ""));
        i.putExtra("isAmOrPm",sp.getString("dateAmOrPm",""));
        i.putExtra("currentState", sp.getInt("dateState", -1));
        i.putExtra("patient", sp.getString("patientName", ""));
    }

    private void setBtn_cancel_unclickable() {
//        GradientDrawable gd = (GradientDrawable) btn_startOrder.getBackground();
//        ObjectAnimator oa = ObjectAnimator.ofArgb(gd, "Color", 0xffbcbcbc, 0xff4385f6);
//        oa.setDuration(1000);
//        oa.start();
//
//        GradientDrawable gd1 = (GradientDrawable) btn_calcelOrder.getBackground();
//        ObjectAnimator oa1 = ObjectAnimator.ofArgb(gd1, "Color", 0xfffecd40, 0xffbcbcbc);
//        oa1.setDuration(1000);
//        oa1.start();

        btn_calcelOrder.setBackgroundResource(R.drawable.btn_gray_enable);
        btn_calcelOrder.setClickable(false);
        btn_startOrder.setBackgroundResource(R.drawable.btn_blue_able);
        btn_startOrder.setClickable(true);
    }

    private void setBtn_start_unclickable() {
//        GradientDrawable gd = (GradientDrawable) btn_startOrder.getBackground();
//        ObjectAnimator oa = ObjectAnimator.ofArgb(gd, "Color", 0xff4385f6, 0xffbcbcbc);
//        oa.setDuration(1000);
//        oa.start();
//
//        GradientDrawable gd1 = (GradientDrawable) btn_calcelOrder.getBackground();
//        ObjectAnimator oa1 = ObjectAnimator.ofArgb(gd1, "Color", 0xffbcbcbc, 0xfffecd40);
//        oa1.setDuration(1000);
//        oa1.start();
        btn_startOrder.setBackgroundResource(R.drawable.btn_gray_enable);
        btn_startOrder.setClickable(false);
        btn_calcelOrder.setBackgroundResource(R.drawable.btn_orange_enable);
        btn_calcelOrder.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        if(Md5.checkServiceisRunning(this)) {
            setNotifiy();
        }
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFY_FLAG);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFY_FLAG);
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if(Md5.checkServiceisRunning(this)) {
            setNotifiy();
        }
        super.onPause();
    }

    int NOTIFY_FLAG = 0x10000;
    private void setNotifiy() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        i.putExtra("isServiceRunning",true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notify = new Notification.Builder(this).setSmallIcon(R.drawable.logo)
                .setTicker("正在尝试预约...")
                .setContentTitle("挂号app")
                .setContentText("正在预约...").setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent).getNotification();
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(NOTIFY_FLAG,notify);
    }


    private void initDialog(String title,String msg) {
        AlertDialog.Builder dia = new AlertDialog.Builder(MainActivity.this);
        dia.setTitle(title);
        dia.setMessage(msg);
        dia.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dia.create().show();
    }

    class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("orderSuccess")){
                initDialog("预约成功!","您的预约已经成功，取消预约请到www.guahao.com取消。");
                setBtn_cancel_unclickable();
            }else if(intent.getAction().equals("orderFail")){
                String reason = intent.getExtras().getString("reason");
                initDialog("预约失败",reason);
                setBtn_cancel_unclickable();
            }
        }


    }


}
