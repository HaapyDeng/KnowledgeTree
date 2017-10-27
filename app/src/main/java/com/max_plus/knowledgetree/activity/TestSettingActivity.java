package com.max_plus.knowledgetree.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.InputScorePopWin;

public class TestSettingActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_score, tv_score_text, tv_grade, tv_grade_text, tv_count, tv_count_text;
    private Button btn_start_test;
    private InputScorePopWin inputScorePopWin;
    private WindowManager.LayoutParams params;
    private String s, score;
    private static final int SET_SCORE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_setting);
        initViews();
    }

    private void initViews() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        tv_score = findViewById(R.id.tv_score);
        tv_score.setOnClickListener(this);

        tv_score_text = findViewById(R.id.tv_score_text);

        tv_grade = findViewById(R.id.tv_grade);
        tv_grade.setOnClickListener(this);

        tv_grade_text = findViewById(R.id.tv_grade_text);

        tv_count = findViewById(R.id.tv_count);
        tv_count.setOnClickListener(this);

        tv_count_text = findViewById(R.id.tv_count_text);

        btn_start_test = findViewById(R.id.btn_start_test);
        btn_start_test.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_score:
                inputScorePopWin = new InputScorePopWin(this, itemsOnClick);
                //显示窗口
                inputScorePopWin.showAtLocation(TestSettingActivity.this.findViewById(R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                params = getWindow().getAttributes();
                //当弹出Popupwindow时，背景变半透明
                params.alpha = 0.7f;
                getWindow().setAttributes(params);
                //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
                inputScorePopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        params = getWindow().getAttributes();
                        params.alpha = 1f;
                        getWindow().setAttributes(params);
                    }
                });
                break;
            case R.id.tv_grade:
                break;
            case R.id.tv_count:
                break;
            case R.id.btn_start_test:
                break;
        }

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_confirm:
                    s = InputScorePopWin.score;
                    Log.d("s==>>>>>>", s);
                    if (s.length() != 0) {
                        Message message = myHandle.obtainMessage();
                        message.what = SET_SCORE;
                        message.obj = s;
                        myHandle.sendMessage(message);
                        inputScorePopWin.dismiss();
                    }
                    break;
            }


        }

    };

    @SuppressLint("HandlerLeak")
    Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_SCORE:
                    score = (String) msg.obj;
                    tv_score_text.setText(score);
                    break;
            }
        }
    };
}
