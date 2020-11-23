package com.leejunj.prism.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.leejunj.prism.SkinManager;
import com.leejunj.prism.type.SkinType;
import com.leejunj.prism.type.SkinTypeSuffix;

import androidx.annotation.Nullable;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/19<p>
 * <p>description : <p>
 */
public class SecondActivity extends BaseActivity{

    private Button btn1, btn2;
    private Button mBtnGo, mBtnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        initView();
    }

    private void initView() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        btn1.setOnClickListener((v) -> {
            SkinManager.getInstance().restoreSkin();
        });
        btn2.setOnClickListener((v) -> {
            boolean b = SkinManager.getInstance().changeSkin(new SkinTypeSuffix("_skin1"));
            Toast.makeText(this, "操作成功？" + b, Toast.LENGTH_SHORT).show();
        });

        mBtnGo = findViewById(R.id.btn_go);
        mBtnBack = findViewById(R.id.btn_back);

        mBtnGo.setOnClickListener((v) -> {
            Intent intent = new Intent(getBaseContext(), ThirdActivity.class);
            startActivity(intent);
        });
        mBtnBack.setOnClickListener((v) -> {
            onBackPressed();
        });
    }
}
