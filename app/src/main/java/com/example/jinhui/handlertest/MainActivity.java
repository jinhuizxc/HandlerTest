package com.example.jinhui.handlertest;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * handler
 *
 * 异常1：
 * Process: com.example.jinhui.handlertest, PID: 3915
 *     android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
 *         at android.view.ViewRootImpl.checkThread(ViewRootImpl.java:7697)
 *         at android.view.ViewRootImpl.invalidateChildInParent(ViewRootImpl.java:1422)
 *
 */
public class MainActivity extends AppCompatActivity {

    TextView textView;
    Handler handler = new Handler();

    ImageView imageView;

    private int images[] = {R.drawable.img1, R.drawable.img2, R.drawable.img3};

    private int index;

    private MyRunnable myRunnable = new MyRunnable();

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            index++;
            index = index % 3; // 求余0、1、2
            imageView.setImageResource(images[index]);
            handler.postDelayed(myRunnable, 1000);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.img);

        // 测试2
//        handler.postDelayed(myRunnable, 1000);

        // 测试1
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
////                    textView.setText("设置文本");  // 不允许这样更新ui
//                    // 创建handler
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            textView.setText("设置文本");  // 可以设置文本
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }
}
