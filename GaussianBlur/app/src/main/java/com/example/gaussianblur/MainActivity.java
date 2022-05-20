package com.example.gaussianblur;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("OpenCLDriver");
    }
    //blur CPU
    public native Bitmap GaussianBlurBitmap(Bitmap bitmap);
    //blur GPU
    //public native Bitmap GaussianBlurGPU(Bitmap bitmap);

    ImageView imgV;
    Bitmap buf_bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv =(TextView) findViewById(R.id.txt);
        imgV =(ImageView) findViewById(R.id.imageView);
        Button btn = (Button) findViewById(R.id.button);
        Button btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;


        buf_bitmap=BitmapFactory.decodeFile("/data/local/tmp/lena.bmp", options);

        tv.setText("Image Processing App");

        imgV.setImageBitmap(buf_bitmap);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                float start = (float)System.nanoTime()/1000000;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                buf_bitmap = GaussianBlurBitmap(buf_bitmap);
                imgV.setImageBitmap(buf_bitmap);

                float end = (float)System.nanoTime()/1000000;
                float timesub = end-start;

                String ts = Float.toString(timesub);
                tv.setText("Exectuion Time: " + ts);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                buf_bitmap = BitmapFactory.decodeFile(".data/local/tmp/lena.bmp", options);
                imgV.setImageBitmap(buf_bitmap);
                tv.setText("Original image");
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}