package com.max_plus.knowledgetree.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.InputGradeAndCountPopWin;
import com.max_plus.knowledgetree.tools.InputScorePopWin;
import com.max_plus.knowledgetree.tools.NetworkUtils;
import com.max_plus.knowledgetree.tools.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.max_plus.knowledgetree.tools.AllToast.doToast;

public class TestSettingActivity extends Activity implements View.OnClickListener, TextWatcher {
    private ImageView iv_back;
    private TextView tv_score, tv_score_text, tv_grade, tv_grade_text, tv_count, tv_count_text, tv_choose_grade;
    private Button btn_start_test;
    private InputScorePopWin inputScorePopWin;
    private WindowManager.LayoutParams params;
    private String s = "", score = "";
    private String grade = "";
    private InputGradeAndCountPopWin inputGradeAndCountPopWin;
    private WheelView wheelView;
    private EditText tv1;
    private int gradeId = 0;
    private int courseId = 1;
    private int count = 0;
    private static final int SET_SCORE = 0;
    private static final int SET_GRADE = 1;
    private static final int SET_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_setting);
        initViews();
//        initDate();
    }

    //获取自测题数据参数
    private void initDate() {
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            doToast(this, getResources().getString(R.string.isNotNetWork));
            return;
        }
        String url = NetworkUtils.returnUrl() + NetworkUtils.returnSelfTestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        JSONArray countArray = data.getJSONArray("count");
                        JSONArray courseArray = data.getJSONArray("course");
                        JSONArray gradeArray = data.getJSONArray("grade");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                doToast(TestSettingActivity.this, getResources().getString(R.string.sever_busy));
                return;
            }
        });
    }

    private void initViews() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        tv_score = findViewById(R.id.tv_score);

        tv_score_text = findViewById(R.id.tv_score_text);
        tv_score_text.setOnClickListener(this);
        tv_score_text.addTextChangedListener(this);

        tv_grade = findViewById(R.id.tv_grade);

        tv_grade_text = findViewById(R.id.tv_grade_text);
        tv_grade_text.setOnClickListener(this);
        tv_grade_text.addTextChangedListener(this);

        tv_count = findViewById(R.id.tv_count);

        tv_count_text = findViewById(R.id.tv_count_text);
        tv_count_text.setOnClickListener(this);
        tv_count_text.addTextChangedListener(this);

        btn_start_test = findViewById(R.id.btn_start_test);
        btn_start_test.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //选择分数
            case R.id.tv_score_text:

                inputScorePopWin = new InputScorePopWin(this, itemsOnClickScore);

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
            //选择年级
            case R.id.tv_grade_text:
                View wh = LayoutInflater.from(this).inflate(R.layout.input_grade_pop, null);
                tv_choose_grade = wh.findViewById(R.id.tv_choose_grade);
                tv_choose_grade.setText(R.string.choose_grade);
                wheelView = wh.findViewById(R.id.wheel);
                wheelView.addData("高一");
                wheelView.addData("高二");
                wheelView.addData("高三");
                wheelView.setCenterItem(1);
                inputGradeAndCountPopWin = new InputGradeAndCountPopWin(this, itemsOnClickGrade, wh);
                inputGradeAndCountPopWin.showAtLocation(TestSettingActivity.this.findViewById(R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                params = getWindow().getAttributes();
                //当弹出Popupwindow时，背景变半透明
                params.alpha = 0.7f;
                getWindow().setAttributes(params);
                //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
                inputGradeAndCountPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        params = getWindow().getAttributes();
                        params.alpha = 1f;
                        getWindow().setAttributes(params);
                    }
                });
                break;
            case R.id.tv_count_text:
                View wh2 = LayoutInflater.from(this).inflate(R.layout.input_grade_pop, null);
                tv_choose_grade = wh2.findViewById(R.id.tv_choose_grade);
                tv_choose_grade.setText(R.string.choose_count);
                wheelView = wh2.findViewById(R.id.wheel);
                wheelView.addData("10道（预计20分钟完成）");
                wheelView.addData("20道（预计40分钟完成）");
                wheelView.addData("30道（预计60分钟完成）");
                wheelView.setCenterItem(1);
                inputGradeAndCountPopWin = new InputGradeAndCountPopWin(this, itemsOnClickCount, wh2);
                inputGradeAndCountPopWin.showAtLocation(TestSettingActivity.this.findViewById(R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                params = getWindow().getAttributes();
                //当弹出Popupwindow时，背景变半透明
                params.alpha = 0.7f;
                getWindow().setAttributes(params);
                //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
                inputGradeAndCountPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        params = getWindow().getAttributes();
                        params.alpha = 1f;
                        getWindow().setAttributes(params);
                    }
                });
                break;
            case R.id.btn_start_test:

                break;
        }

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClickScore = new View.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_confirm:
                    tv1 = inputScorePopWin.getContentView().findViewById(R.id.et_score);
                    if (tv1.equals("")) {
                        inputScorePopWin.dismiss();
                    } else {
                        score = tv1.getText().toString().trim();
                        Message message = myHandle.obtainMessage();
                        message.what = SET_SCORE;
                        message.obj = score;
                        myHandle.sendMessage(message);
                        inputScorePopWin.dismiss();

                    }
                    break;
            }


        }

    };

    private View.OnClickListener itemsOnClickGrade = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_delete:
                    inputGradeAndCountPopWin.dismiss();
                    break;
                case R.id.tv_over:
                    String date;
                    date = wheelView.getCenterItem().toString();
                    if (date.equals("高一")) {
                        gradeId = 1;
                    } else if (date.equals("高二")) {
                        gradeId = 2;
                    } else {
                        gradeId = 3;
                    }
                    Log.d("date==", "" + wheelView.getCenterItem());
                    Message message = myHandle.obtainMessage();
                    message.what = SET_GRADE;
                    message.obj = date;
                    myHandle.sendMessage(message);
                    inputGradeAndCountPopWin.dismiss();
                    break;
            }
        }
    };
    private View.OnClickListener itemsOnClickCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_delete:
                    inputGradeAndCountPopWin.dismiss();
                    break;
                case R.id.tv_over:
                    String date;
                    date = wheelView.getCenterItem().toString();
                    if (date.equals("10道（预计20分钟完成）")) {
                        count = 10;
                    } else if (date.equals("20道（预计40分钟完成）")) {
                        count = 20;
                    } else {
                        count = 30;
                    }
                    Log.d("date==", "" + wheelView.getCenterItem());
                    Message message = myHandle.obtainMessage();
                    message.what = SET_COUNT;
                    message.obj = count;
                    myHandle.sendMessage(message);
                    inputGradeAndCountPopWin.dismiss();
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
                case SET_GRADE:
                    grade = (String) msg.obj;
                    tv_grade_text.setText(grade);
                    break;
                case SET_COUNT:
                    count = (int) msg.obj;
                    tv_count_text.setText("" + count);
                    break;
            }
        }
    };

    //监听textView内容改变按钮状态
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int t) {
        if (!score.equals("") && !grade.equals("") && count != 0) {
            btn_start_test.setClickable(true);
            btn_start_test.setBackground(getResources().getDrawable(R.drawable.shape_corner_blue));
        } else {
            btn_start_test.setClickable(false);
            btn_start_test.setBackground(getResources().getDrawable(R.drawable.shape_corner_grey));
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
