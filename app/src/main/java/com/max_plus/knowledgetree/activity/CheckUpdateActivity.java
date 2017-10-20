package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.max_plus.knowledgetree.R;

public class CheckUpdateActivity extends Activity implements View.OnClickListener {
    private LinearLayout ll_new_version, ll_check_update, ll_update_button;
    private ImageView iv_back, iv_update;
    private TextView tv_old_version, new_version, tv_version, tv_version_content;
    private Button btn_update;
    private String oldVersion, newVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_update);
        initView();
    }

    private void initView() {
        ll_new_version = findViewById(R.id.ll_new_version);
        ll_check_update = findViewById(R.id.ll_check_update);
        ll_update_button = findViewById(R.id.ll_update_button);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        iv_update = findViewById(R.id.iv_update);
        iv_update.setOnClickListener(this);

        tv_old_version = findViewById(R.id.tv_old_version);
        new_version = findViewById(R.id.new_version);
        tv_version = findViewById(R.id.tv_version);
        tv_version_content = findViewById(R.id.tv_version_content);

        btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_update:
//                getOldVersion();
//                getNewVersion();
//                if (newVersion.equals(oldVersion)) {
//                    iv_update.setBackground(getResources().getDrawable(R.drawable.my_ic_update_grey));
//                    tv_version.setText("你的" + oldVersion + ",已是最新版本");
//                } else {
//                    ll_new_version.setVisibility(View.VISIBLE);
//                    ll_update_button.setVisibility(View.VISIBLE);
//                    ll_check_update.setVisibility(View.INVISIBLE);
//                }
                break;
            case R.id.btn_update:
                break;
        }
    }

    private void getNewVersion() {
    }

    private void getOldVersion() {
    }
}
