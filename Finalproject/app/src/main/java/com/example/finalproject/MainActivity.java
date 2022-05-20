package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements JNIListener {

    static {
        System.loadLibrary("JNIDriver");
    }


    ImageView imgv;
    ImageView d1,d2;
    boolean start = false;
    String str="";
    int i,k;
    int w = 0;

    JNIDriver mDriver;

    boolean mThreadRun = true;

    int data_int=0;
    boolean mThreadRun1;
    boolean mStart;
    SegmentThread mSegThread;

    private native static int openDriver1(String path);
    private native static void closeDriver1();
    private native static void writeDriver1(byte[] data, int length);

    private native static int openDriver2(String path);
    private native static void closeDriver2();
    private native static void writeDriver2(byte[] data, int length);


    byte[] data1 = {0,0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgv = (ImageView) findViewById(R.id.imageView);
        d1=(ImageView) findViewById(R.id.ddong1);
        d2=(ImageView) findViewById(R.id.ddong2);
        d1.setX(40);
        d1.setY(20);
        d2.setX(500);
        d2.setY(20);


        mDriver = new JNIDriver();
        mDriver.setListener(this);


        if(mDriver.open("/dev/sm9s5422_interrupt")<0){
            Toast.makeText(MainActivity.this, "Driver Open Failed", Toast.LENGTH_SHORT).show();
        }

        if(openDriver1("/dev/sm9s5422_led")<0) {
            Toast.makeText(MainActivity.this, "Driver Open Failed", Toast.LENGTH_SHORT).show();
        }

    }

    private class SegmentThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (mThreadRun1){
                byte[] n= {0,0,0,0,0,0,0};

                if(mStart==false) { writeDriver2(n, n.length); }
                else {
                    for(i=0; i<100; i++) {
                        n[0] = (byte) (data_int % 1000000 / 100000);
                        n[1] = (byte) (data_int % 100000 / 10000);
                        n[2] = (byte) (data_int % 10000 / 1000);
                        n[3] = (byte) (data_int % 1000 / 100);
                        n[4] = (byte) (data_int % 100 / 10);
                        n[5] = (byte) (data_int % 10);
                        writeDriver2(n, n.length);
                    }

                    if(data_int<1) {
                        data1[w] = 0;
                        writeDriver1(data1, data1.length);
                        data_int = 500;
                        w++;
                    }


                }
            }
        }
    }


    @Override
    protected void onPause() {
        closeDriver2();
        closeDriver1();
        mDriver.close();
        mThreadRun=false;
        mSegThread=null;
        super.onPause();
    }

    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("START!")
                .setMessage("GAME START!!")
                .setPositiveButton("GO~!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        start = true;
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }




    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1){

                case 1: //up
                    data_int +=100;
                    break;
                case 2: //down
                    data_int -= 100;
                    break;
                case 3: //left
                    imgv.setX(640);
                    imgv.setY(900);
                    TranslateAnimation anim = new TranslateAnimation
                            (Animation.RELATIVE_TO_SELF,   // fromXDelta
                                    Animation.RELATIVE_TO_SELF-600.0f,  // toXDelta
                                    Animation.RELATIVE_TO_SELF,    // fromYDelta
                                    Animation.RELATIVE_TO_SELF);// toYDelta
                    anim.setDuration(500);
                    anim.setFillAfter(true);
                    imgv.startAnimation(anim);
                    k=1;
                    break;
                case 4: //right
                    imgv.setX(40);
                    imgv.setY(900);
                    TranslateAnimation anim2 = new TranslateAnimation
                            (Animation.RELATIVE_TO_SELF,   // fromXDelta
                                    Animation.RELATIVE_TO_SELF+600.0f,  // toXDelta
                                    Animation.RELATIVE_TO_SELF,    // fromYDelta
                                    Animation.RELATIVE_TO_SELF);// toYDelta
                    anim2.setDuration(500);
                    anim2.setFillAfter(true);
                    imgv.startAnimation(anim2);
                    k=2;
                    break;
                case 5: //center
                    showDialog();
                    TranslateAnimation anim3 = new TranslateAnimation
                            (Animation.RELATIVE_TO_SELF,   // fromXDelta
                                    Animation.RELATIVE_TO_SELF,  // toXDelta
                                    20.0f,    // fromYDelta
                                    1120.0f);// toYDelta
                    anim3.setDuration(5000);
                    anim3.setStartOffset(2000);
                    anim3.setRepeatCount(-1);
                    TranslateAnimation anim4 = new TranslateAnimation
                            (Animation.RELATIVE_TO_SELF,   // fromXDelta
                                    Animation.RELATIVE_TO_SELF,  // toXDelta
                                    20.0f,    // fromYDelta
                                    1120.0f);// toYDelta
                    anim4.setDuration(5000);
                    anim4.setStartOffset(1000);
                    anim4.setRepeatCount(-1);

                    float time = (float)System.currentTimeMillis()/1000;
                    d1.startAnimation(anim3);
                    d2.startAnimation(anim4);
                    imgv.setX(40);
                    imgv.setY(900);
                    for(int i=0; i<8;i++){
                        data1[i] = 1;
                    }
                    writeDriver1(data1, data1.length);
                    mStart = true;
                    data_int = 100;
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        if(openDriver1("/dev/sm9s5422_led")<0) {
            Toast.makeText(MainActivity.this, "Driver Open Failed", Toast.LENGTH_SHORT).show();
        }
        if(openDriver2("/dev/sm9s5422_segment")<0){
            Toast.makeText(MainActivity.this, "Driver Open Failed", Toast.LENGTH_SHORT).show();
        }
        mThreadRun=true;
        mThreadRun1=true;
        mSegThread=new SegmentThread();
        mSegThread.start();
        super.onResume();
    }

    @Override
    public void onReceive(int val) {
        Message text = Message.obtain();
        text.arg1=val;
        handler.sendMessage(text);
    }

}