package com.example.wf.keng;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;

public class InputDialog extends AlertDialog implements OnClickListener{
    private EditText etPassword;  //编辑框
    private Button btnConfrim;  //确定按钮
    private Button btnCancel;  //取消按钮
    private OnEditInputFinishedListener mListener; //接口
    public static double height;

    public interface OnEditInputFinishedListener{
        void editInputFinished(String password);
    }

    protected InputDialog(Context context, OnEditInputFinishedListener mListener) {
        super(context);
        this.mListener = mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        //控件
        etPassword = (EditText)findViewById(R.id.et_password);
        btnConfrim = (Button)findViewById(R.id.btn_confirm);
        btnCancel = (Button)findViewById(R.id.btn_cancel);

        btnConfrim.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            //确定
            if (mListener != null) {
                String height = etPassword.getText().toString();
                mListener.editInputFinished(height);
                Intent startSize = new Intent(getContext(), Size.class);
                startSize.putExtra("data",height);
                getContext().startActivity(startSize);
            }
            dismiss();
        }else {
            //取消
            if (mListener != null) {
                String height = etPassword.getText().toString();
                mListener.editInputFinished(height);
                Intent startDepth = new Intent(getContext(), Depth.class);
                startDepth.putExtra("data",height);
                getContext().startActivity(startDepth);
            }
            dismiss();
        }
    }

}
