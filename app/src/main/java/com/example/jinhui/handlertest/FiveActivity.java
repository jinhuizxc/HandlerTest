package com.example.jinhui.handlertest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Email: 1004260403@qq.com
 * Created by jinhui on 2018/11/17.
 *
 * 更新UI的4种方式
 */
public class FiveActivity extends AppCompatActivity {

    TextView textView;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView.setText("ok");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);

        textView = findViewById(R.id.textView);


        /**
         *  非UI线程中真的不能更新ui么？不是的，可以修改ui，但是线程休眠后更新ui就会出现异常
         *   Process: com.example.jinhui.handlertest, PID: 22471
         *     android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
         *         at android.view.ViewRootImpl.checkThread(ViewRootImpl.java:7697)
         *         at android.view.ViewRootImpl.invalidateChildInParent(ViewRootImpl.java:1422)
         *
         * 查看源码后发现原因是viewRootImpl的创建是在onResume后执行，
         * Thread.sleep(1000);执行后创建好viewRootImpl就会抛出异常
         *
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     * 异常3
                     *   Process: com.example.jinhui.handlertest, PID: 30413
                     *     java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                     *
                     */
//                    Handler handler = new Handler();  // 注：在进行handler时必须有looper对象，否则会出现上面的问题！
                    Thread.sleep(1000);
//                    textView.setText("ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    Thread.sleep(1000);
////                    handler1();  // 方式一
////                    handler2();   // 方式二
////                    updateUI();   // 方式三
////                    viewUI();   // 方式四
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    private void viewUI() {
       textView.post(new Runnable() {
           @Override
           public void run() {
               textView.setText("ok");
           }
       });
    }

    private void updateUI() {
      runOnUiThread(new Runnable() {
          @Override
          public void run() {
              textView.setText("ok");
          }
      });
    }

    private void handler2() {
        handler.sendEmptyMessage(1);
    }

    // 方式一
    private void handler1(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("ok");
            }
        });
    }
}
