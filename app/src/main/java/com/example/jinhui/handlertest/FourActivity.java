package com.example.jinhui.handlertest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Email: 1004260403@qq.com
 * Created by jinhui on 2018/11/17.
 *
 * 主线程给子线程发送消息
 */
public class FourActivity extends AppCompatActivity implements View.OnClickListener {

    // 子线程handler
    private Handler threadHandler;

    // 主线程handler
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Message message = new Message();
            System.out.println("main handler");
            // 向子子线程发送消息
            threadHandler.sendMessageDelayed(message, 1000);
        }
    };

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        HandlerThread thread = new HandlerThread("handlerThread");
        thread.start();

        threadHandler = new Handler(thread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Message message = new Message();
                System.out.println("thread handler");
                // 向主线程发送消息
                handler.sendMessageDelayed(message, 1000);
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                handler.sendEmptyMessage(1);
                break;
            case R.id.button2:
                handler.removeMessages(1);
                break;
        }
    }
}
