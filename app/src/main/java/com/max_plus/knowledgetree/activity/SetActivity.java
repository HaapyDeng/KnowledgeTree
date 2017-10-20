package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.max_plus.knowledgetree.R;

public class SetActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_about, tv_check_update, tv_clear_cache, tv_cache;
    private Button btn_login_out;
    private String cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        tv_about = findViewById(R.id.tv_about);
        tv_about.setOnClickListener(this);

        tv_check_update = findViewById(R.id.tv_check_update);
        tv_check_update.setOnClickListener(this);

        tv_clear_cache = findViewById(R.id.tv_clear_cache);
        tv_clear_cache.setOnClickListener(this);

        tv_cache = findViewById(R.id.tv_cache);

        btn_login_out = findViewById(R.id.btn_login_out);
        btn_login_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_about:
                Intent aboutIntent = new Intent();
                aboutIntent.setClass(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.tv_check_update:
                break;
            case R.id.tv_clear_cache:
                break;
            case R.id.btn_login_out:
                doLoginOut();
                break;
        }
    }

    private void doLoginOut() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
