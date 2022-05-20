package com.example.api_camera;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Surface;
import android.view.View;
import android.widget.*;
import android.widget.FrameLayout;
import android.hardware.Camera.PictureCallback;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CamTestActivity";

    private Camera mCamera;
    private CameraPreview mPreview;
    private ImageView capturedImageHolder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button_capture);
        capturedImageHolder = (ImageView)findViewById(R.id.captured_image);

        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(180);

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, pictureCallback);
            }
        });
    }


    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){

        }
        return c;
    }

    PictureCallback pictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            int w = options.outWidth;
            int h = options.outHeight;

            //Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            //int w = bitmap.getWidth();
            //int h = bitmap.getHeight();

            Matrix mtx = new Matrix();
            mtx.postRotate(180);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);

            if(bitmap==null){
                Toast.makeText(MainActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }
            capturedImageHolder.setImageBitmap(scaleDownBitmapImage(rotatedBitmap, 450, 300 ));
        }
    };
    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }


    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamera();
    }

    private void releaseMediaRecorder() { mCamera.lock(); }

    private void releaseCamera(){
        if (mCamera != null) {
            mCamera.release();
            mCamera=null;
        }
    }
}