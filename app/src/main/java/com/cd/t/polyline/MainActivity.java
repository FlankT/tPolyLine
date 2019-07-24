package com.cd.t.polyline;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    PolyLine polyline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        polyline = findViewById(R.id.polyline);
    }

    /**
     * 数据模拟
     */
    final float[] pointArray = {
            26.211f, 26.212f, 26.210f, 26.215f, 26.215f, 26.213f, 26.214f, 26.218f, 26.215f, 26.218f,
            26.211f, 26.212f, 26.210f, 26.215f, 26.215f, 26.213f, 26.214f, 26.218f, 26.215f, 26.218f,
            26.211f, 26.212f, 26.210f, 26.215f, 26.215f, 26.213f, 26.214f, 26.218f, 26.215f, 26.218f,
            26.211f, 26.212f, 26.210f, 26.215f, 26.215f, 26.213f, 26.214f, 26.218f, 26.215f, 26.218f,
            26.211f, 26.212f, 26.210f, 26.215f, 26.215f, 26.213f, 26.214f, 26.218f, 26.215f, 26.218f,
            26.211f, 26.212f, 26.210f, 26.215f, 26.215f, 26.213f, 26.214f, 26.218f, 26.215f, 26.217f};
    final float[] pointArray2 = {
            26.210f, 26.211f, 26.208f, 26.213f, 26.213f, 26.210f, 26.211f, 26.216f, 26.213f, 26.204f,
            26.210f, 26.211f, 26.208f, 26.213f, 26.213f, 26.210f, 26.211f, 26.216f, 26.213f, 26.204f,
            26.210f, 26.211f, 26.208f, 26.213f, 26.213f, 26.210f, 26.211f, 26.216f, 26.213f, 26.204f,
            26.210f, 26.211f, 26.208f, 26.213f, 26.213f, 26.210f, 26.211f, 26.216f, 26.213f, 26.204f,
            26.210f, 26.211f, 26.208f, 26.213f, 26.213f, 26.210f, 26.211f, 26.216f, 26.213f, 26.204f,
            26.210f, 26.211f, 26.208f, 26.213f, 26.213f, 26.210f, 26.211f, 26.216f, 26.213f, 26.204f};

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {     //在线程中不断往集合中增加数据
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i < 60) {
                        if (null != pointArray) {
                            polyline.addData(pointArray[i]);
                        }
                        if (null != pointArray) {
                            polyline.addSecondData(pointArray2[i]);
                        }
                        mh.sendEmptyMessage(0);   //发送空消息通知刷新
                    }
                }
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    private Handler mh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {    //判断接受消息类型
                polyline.invalidate();  //刷新View
            }
        }
    };
}
