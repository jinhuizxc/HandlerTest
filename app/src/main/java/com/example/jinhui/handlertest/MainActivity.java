package com.example.jinhui.handlertest;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
//    Handler handler = new Handler();
    Button bt_remove;

    // 测试3，发送消息
//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
////            textView.setText(msg.arg1 + "-" + msg.arg2);
//            textView.setText(msg.obj + "");
//        }
//    };

    // 测试4 callBack
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "" + 1, Toast.LENGTH_SHORT).show();
            return true;  // 补充: false 不截获消息，弹出toast 1-2， true截获消息 弹出1，不弹出2
        }
    }){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getApplicationContext(), "" + 2, Toast.LENGTH_SHORT).show();

        }
    };

    class Person{
        public String name;
        public int age;

        @NonNull
        @Override
        public String toString() {
            return "name = " + name + " " + "age = " + age;
        }
    }
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
        bt_remove = findViewById(R.id.button);

        // 移除handler
        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.removeCallbacks(myRunnable);
                // 测试callBack
                handler.sendEmptyMessage(1);
            }
        });

        // 测试3
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
//                    Message message = new Message();
//                    message.arg1 = 88;
//                    message.arg2 = 100;
                    // 复用系统的message对象
                    Message message = handler.obtainMessage();

                    // 发送大量的数据时；
                    Person person = new Person();
                    person.name = "jinhui";
                    person.age = 16;
                    message.obj = person;
//                    handler.sendMessage(message);
                    message.sendToTarget();  // 通过message发送消息
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();

        // 测试2 图片轮播
        handler.postDelayed(myRunnable, 1000);

        // 测试1 更新UI
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
