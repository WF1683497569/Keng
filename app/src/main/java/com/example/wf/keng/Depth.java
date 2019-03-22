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

public class Depth extends Activity implements SensorEventListener{

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
    private TextView value3;
    private Button first_record;
    private Button second_record;
    private Button third_record;
    private Button result;
    private static double vX1;
    private static double vY1;
    //    private static double vZ1;
    private static double vX2;
    private static double vY2;
    //    private static double vZ2;
    private static double vX3;
    private static double vY3;
    //    private static double vZ3;
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
        setContentView(R.layout.activity_depth);
//        vX = (TextView) findViewById(R.id.vx);
//        vY = (TextView) findViewById(R.id.vy);
//        vZ = (TextView) findViewById(R.id.vz);
        re = (TextView) findViewById(R.id.re);
        value1 = (TextView) findViewById(R.id.value1);
        value2 = (TextView) findViewById(R.id.value2);
        value3 = (TextView) findViewById(R.id.value3);
        first_record = (Button) findViewById(R.id.first);
        second_record = (Button) findViewById(R.id.second);
        third_record = (Button) findViewById(R.id.third);
        result = (Button) findViewById(R.id.result);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        h = Double.parseDouble(data);

        //点击按钮1记录C点数据
        first_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test数据
//                vX1 = ;
//                vY1 = ;
                vX1 = X;
                vY1 = Y;
//                vZ1 = Z;
                value1.setText("第一次记录：" + vX1 + "/" + vY1 + "");
                Toast.makeText(Depth.this, "坑底点记录成功！", Toast.LENGTH_SHORT).show();
            }
        });

        //点击按钮2记录B点数据
        second_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test数据
//                vX2 = ;
//                vY2 = ;
                vX2 = X;
                vY2 = Y;
//                vZ2 = Z;
                value2.setText("第二次记录：" + vX2 + "/" + vY2 + "");
                Toast.makeText(Depth.this, "边缘点记录成功！", Toast.LENGTH_SHORT).show();
            }
        });

        //点击按钮3记录B'点数据
        third_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test数据
//                vX3 = ;
//                vY3 = ;
                vX3 = X;
                vY3 = Y;
//                vZ2 = Z;
                value3.setText("第三次记录：" + vX3 + "/" + vY3 + "");
                Toast.makeText(Depth.this, "正上方记录成功！", Toast.LENGTH_SHORT).show();
            }
        });

        //点击按钮求深度
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double result = getDeep(vY1,vY2,vX3,vX2-vX1,h);
                Toast.makeText(Depth.this, "正在计算...", Toast.LENGTH_SHORT).show();
                re.setText("此道路坑洞的尺寸为：" + result +"");
                Toast.makeText(Depth.this, "计算成功...", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(Depth.this, ShowPhoto.class);
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
    public double getDeep(double a, double b, double c, double d, double h) {
        //测试数据：a = 46.02  b = 47.84   c = 48.86   d = 57 h = 1
        double a0 = Math.tan(Math.toRadians(a));
        double b0 = Math.tan(Math.toRadians(b));
        double c0 = Math.tan(Math.toRadians(c));
        double d0 = Math.cos(Math.toRadians(d));
        double h0 = h;
        //分子
        double up = Math.sqrt(a0*a0*d0*d0+c0*c0-a0*a0)-a0*d0;
        //分母3
        double down = c0*c0-a0*a0;
        //计算结果
        double cc = (h0*b0*up)/down-h0;

        return cc;
    }


}
