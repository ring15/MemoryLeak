package com.example.memoryleak;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LeakActivity extends AppCompatActivity {

    private Button mMainBtn;
//    private WebView mWebView;
    private MyReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);
        mMainBtn = findViewById(R.id.btn_main);
//        mWebView = findViewById(R.id.webview);
        File file = new File(getExternalFilesDir(""), "123.txt");
        byte[] arrays = new byte[2];
        byte[] a = new byte[1000];
        for (int i = 0; i < 1000; i++){
            a[i] = 0;
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(a);
            outputStream.flush();
            outputStream.close();
            InputStream inputStream = new FileInputStream(file);
            while (inputStream.read(arrays) != -1){
                Log.i("input","input");
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LeakActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
