package com.example.jinhui.handlertest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Email: 1004260403@qq.com
 * Created by jinhui on 2018/11/17.
 *
 * HandlerThread的使用,用于解决多线程并发的问题
 * 子线程给主线程发送消息
 */
public class ThreeActivity extends AppCompatActivity {

    private HandlerThread handlerThread;

    private Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("handler thread");
        setContentView(textView);
        handlerThread = new HandlerThread("handler thread");
        handlerThread.start();

        // 子线程给主线程发送消息
        handler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("currentThread--------" + Thread.currentThread());  // I/System.out: currentThread--------Thread[handler thread,5,main]
            }
        };

        handler.sendEmptyMessage(1);
    }

}
