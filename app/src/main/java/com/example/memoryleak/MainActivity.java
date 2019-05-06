package com.example.memoryleak;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private TextView mStaticDrawable;
    private static Drawable sDrawable;
    private Button mGoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStaticDrawable = findViewById(R.id.tv_static_drawable);
        mGoto = findViewById(R.id.btn_goto);
        if (sDrawable == null) {
            sDrawable = getResources().getDrawable(R.mipmap.ic_launcher_round);
        }
        mStaticDrawable.setBackground(sDrawable);
        mHandler.postDelayed(mRunnable, 1000000);
//        mHandler.sendEmptyMessageDelayed(0x01,1000000);
        mGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LeakActivity.class));
                finish();
            }
        });
    }

//    private MyHandler mHandler = new MyHandler(MainActivity.this);
//    private MyRunnable mRunnable = new MyRunnable();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mStaticDrawable.setText("123");
        }
    };

    private static class MyRunnable implements Runnable{

        @Override
        public void run() {

        }
    }
    private static class MyHandler extends Handler{

        private WeakReference<MainActivity> mReference;

        public MyHandler(MainActivity activity){
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mReference.get() == null){
                return;
            }
            mReference.get().mStaticDrawable.setText("123");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mHandler.removeCallbacksAndMessages(null);
    }
}
