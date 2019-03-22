package com.example.wf.keng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Size extends Activity implements SensorEventListener{

    private CameraPreview mPreview;

//    Z轴暂不需要，已先行注释

    private SensorManager sensorManager = null;
    private Sensor gyroSensor = null;
    private TextView vX;
    private TextView vY;
    //    private TextView vZ;
    private TextView re;
    private TextView value1;
    private TextView value2;
    private Button first_record;
    private Button second_record;
    private Button result;
    private static double vX1;
    private static double vY1;
    //    private static double vZ1;
    private static double vX2;
    private static double vY2;
    //    private static double vZ2;
    public static double X;
    public static double Y;
//    public static double Z;
    private static Double h;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_size);
//        vX = (TextView) findViewById(R.id.vx);
//        vY = (TextView) findViewById(R.id.vy);
//        vZ = (TextView) findViewById(R.id.vz);
        re = (TextView) findViewById(R.id.re);
        value1 = (TextView) findViewById(R.id.value1);
        value2 = (TextView) findViewById(R.id.value2);
        first_record = (Button) findViewById(R.id.first);
        second_record = (Button) findViewById(R.id.second);
        result = (Button) findViewById(R.id.result);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        h = Double.parseDouble(data);


        //点击按钮1记录A点数据
        first_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test数据
//                vX1 = 227;
//                vY1 = 66.98;
                vX1 = X;
                vY1 = Y;
//                vZ1 = Z;
                value1.setText("第一次记录：" + vX1 + "/" + vY1 + "");
                Toast.makeText(Size.this, "已记录第一次位置！", Toast.LENGTH_SHORT).show();
            }
        });

        //点击按钮2记录B点数据
        second_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test数据
//                vX2 = 279;
//                vY2 = 72.00;
                vX2 = X;
                vY2 = Y;
//                vZ2 = Z;
                value2.setText("第二次记录：" + vX2 + "/" + vY2 + "");
                Toast.makeText(Size.this, "已记录第二次位置！", Toast.LENGTH_SHORT).show();

            }
        });

        //点击按钮求尺寸
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double result = getAB(vY1,vY2,vX2-vX1,h);
                Toast.makeText(Size.this, "正在计算...", Toast.LENGTH_SHORT).show();
                re.setText("此道路坑洞的尺寸为：" + result +"");
                Toast.makeText(Size.this, "计算成功...", Toast.LENGTH_SHORT).show();
            }
        });

        initCamera();
        final ImageView mediaPreview = (ImageView) findViewById(R.id.media_preview);
        final Button buttonCapturePhoto = (Button) findViewById(R.id.button_capture_photo);
        final TextView zhunxin = (TextView) findViewById(R.id.zhunxin) ;
        zhunxin.bringToFront();
        buttonCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreview.takePicture(mediaPreview);
            }
        });
        mediaPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Size.this, ShowPhoto.class);
                intent.setDataAndType(mPreview.getOutputMediaFileUri(), mPreview.getOutputMediaFileType());
                startActivityForResult(intent, 0);
            }
        });
    }

    private void initCamera() {
        mPreview = new CameraPreview(this);
        RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this); // 解除监听器注册
        mPreview = null;
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL); //为传感器注册监听器
        if (mPreview == null) {
            initCamera();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        vX.setText("Orientation X: " + event.values[0]);
//        vY.setText("Orientation Y: " + event.values[1]);
//        vZ.setText("Orientation Z: " + event.values[2]);
        X = event.values[0];
        Y = event.values[1];
//        Z = event.values[2];
    }

    //计算尺寸方法
    public  double getAB(double a,double b,double c,double h) {
        double a0 = Math.toRadians(a);
        double b0 = Math.toRadians(b);
        double c0 = Math.toRadians(c);
        double AB = h*Math.sqrt((Math.tan(a0))*(Math.tan(a0))+(Math.tan(b0))*(Math.tan(b0))-2*(Math.tan(a0)*Math.tan(b0)*Math.cos(c0)));
        return AB;
    }


}
