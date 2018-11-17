package com.example.jinhui.handlertest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            System.out.println("currentThread main = " + Thread.currentThread()); // I/System.out: currentThread main = Thread[main,5,main]
//        }
//    };


    private class MyThread extends Thread {

        public Handler handler;
        public Looper looper;  // 定义一个looper对象
        @SuppressLint("HandlerLeak")
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            looper = Looper.myLooper();
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

    private Handler handler;
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
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        myThread.handler.sendEmptyMessage(1);
//        handler.sendEmptyMessage(1);

        // Handler传入looper对象
        /**
         * 异常2
         *  Process: com.example.jinhui.handlertest, PID: 5294
         *     java.lang.RuntimeException: Unable to start activity ComponentInfo{com.example.jinhui.handlertest/com.example.jinhui.handlertest.SecondActivity}: java.lang.NullPointerException: Attempt to read from field 'android.os.MessageQueue android.os.Looper.mQueue' on a null object reference
         *         at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2878)
         *         at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2940)
         *
         *         原因是new MyThread()在创建的时候没有创建looper对象，导致空指针
         *         ->故此了解引入HandlerThread这个类
         */
        handler = new Handler(myThread.looper){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("输出log = " + msg);
            }
        };

        handler.sendEmptyMessage(1);


    }
}
