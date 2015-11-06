package edu.cqut.cn.guahaoapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dun on 2015/10/13.
 */
public class BaseActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
    AnimatedCircleLoadingView animatedCircleLoadingView;
    AlertDialog processDialog;
    boolean isDialogShowing = false;
    public void showProgressDialog(){
        if(isDialogShowing){
            return;
        }


        View view = LayoutInflater.from(this).inflate(R.layout.processdialog,null);
//        animatedCircleLoadingView = (AnimatedCircleLoadingView)view.findViewById(R.id.circle_loading_view);
//        animatedCircleLoadingView.startIndeterminate();
//
//        animatedCircleLoadingView.setLayoutAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                processDialog.dismiss();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });



        AlertDialog.Builder bulider = new AlertDialog.Builder(this);
        bulider.setView(view);

        processDialog = bulider.create();
        processDialog.setCanceledOnTouchOutside(false);
        processDialog.show();
        isDialogShowing =true;
    }


    static int DIALOG_CUCCESS = 1;
    static int DIALOG_FALI = 0;


    public void closeProcessDialog(int flag){
        if(isDialogShowing = false){
            return;
        }

        if(processDialog==null){
            return;
        }

        processDialog.dismiss();
        isDialogShowing = false;
    }

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
