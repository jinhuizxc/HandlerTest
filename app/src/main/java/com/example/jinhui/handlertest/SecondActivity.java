package com.example.jinhui.handlertest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Email: 1004260403@qq.com
 * Created by jinhui on 2018/11/17.
 */
public class SecondActivity extends AppCompatActivity {

    private MyThread myThread;

    /**
     * handleMessage中不要处理耗时操作，导致UI界面出现卡死现象
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            System.out.println("currentThread main = " + Thread.currentThread()); // I/System.out: currentThread main = Thread[main,5,main]
        }
    };


    private class MyThread extends Thread {

        public Handler handler;
        @SuppressLint("HandlerLeak")
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    System.out.println("currentThread = " + Thread.currentThread());  // I/System.out: currentThread = Thread[Thread-2,5,main]
                }
            };

            Looper.loop();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("handler");
        setContentView(textView);
        // threadlocal 在线程中保存变量的信息
        // ActivityThread main Looper MessageQueue

        myThread = new MyThread();
        myThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myThread.handler.sendEmptyMessage(1);
        handler.sendEmptyMessage(1);


    }
}
