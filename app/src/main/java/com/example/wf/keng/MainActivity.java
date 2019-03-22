package com.example.wf.keng;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button size;

    private Button depth;

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
        setContentView(R.layout.activity_main);


        size = (Button) findViewById(R.id.size);
//        depth = (Button) findViewById(R.id.depth);

        size.setOnClickListener(this);
//        depth.setOnClickListener(this);

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
        Toast.makeText(MainActivity.this, "请点击开始测量输入高度！", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.size :
                InputDialog dialog1 = new InputDialog(MainActivity.this, new InputDialog.OnEditInputFinishedListener(){

                    @Override
                    public void editInputFinished(String password) {
                    }
                });
                dialog1.setView(new EditText(MainActivity.this));  //若对话框无法弹出输入法，加上这句话
                dialog1.show();
                break;
//            case R.id.depth:
//                InputDialog dialog2 = new InputDialog(MainActivity.this, new InputDialog.OnEditInputFinishedListener(){
//
//                    @Override
//                    public void editInputFinished(String height) {
//                    }
//                });
//                dialog2.setView(new EditText(MainActivity.this));  //若对话框无法弹出输入法，加上这句话
//                dialog2.show();
//                break;
                default:
        }
    }
}
