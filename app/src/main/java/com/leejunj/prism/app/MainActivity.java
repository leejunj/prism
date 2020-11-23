package com.leejunj.prism.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    private Button mBtnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mBtnGo = findViewById(R.id.btn_go);

        mBtnGo.setOnClickListener((v) -> {
            Intent intent = new Intent(getBaseContext(), SecondActivity.class);
            startActivity(intent);
        });
    }
}