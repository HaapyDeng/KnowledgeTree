package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.max_plus.knowledgetree.R;

public class TestEndActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_auto_enter;
    private Button start_test_info;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_end);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        tv_auto_enter = findViewById(R.id.tv_auto_enter);

        start_test_info = findViewById(R.id.start_test_info);
        start_test_info.setOnClickListener(this);
        timer.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.start_test_info:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("start", 1);
                bundle.putInt("id", id);
                intent.putExtras(bundle);
                intent.setClass(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private CountDownTimer timer = new CountDownTimer(5000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tv_auto_enter.setText((millisUntilFinished / 1000) + "s自动进入");
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("start", 1);
            bundle.putInt("id", id);
            intent.putExtras(bundle);
            intent.setClass(TestEndActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
