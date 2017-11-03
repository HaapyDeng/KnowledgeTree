package com.max_plus.knowledgetree.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.max_plus.knowledgetree.tools.AllToast.doToast;

public class SelfTestQuestionsActivity extends Activity implements View.OnClickListener {
    private int id, count, state = 0, current_count = 1;
    private String exerciseId, answer, content, token;
    private ImageView iv_back;
    private WebView wb_webView;
    private TextView tv_current, tv_total, tv_choose_a, tv_choose_b, tv_choose_c, tv_choose_d;
    private final int WEB_VIEW = 0;
    private long start_time, end_time, millis_time, time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_test_questions);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        wb_webView = findViewById(R.id.wb_web);

        tv_current = findViewById(R.id.tv_current);

        tv_total = findViewById(R.id.tv_total);

        tv_choose_a = findViewById(R.id.tv_choose_a);
        tv_choose_a.setOnClickListener(this);

        tv_choose_b = findViewById(R.id.tv_choose_b);
        tv_choose_b.setOnClickListener(this);

        tv_choose_c = findViewById(R.id.tv_choose_c);
        tv_choose_c.setOnClickListener(this);

        tv_choose_d = findViewById(R.id.tv_choose_d);
        tv_choose_d.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
        count = bundle.getInt("count");
        tv_current.setText("" + current_count);
        tv_total.setText("" + count);
        initData();

    }

    private void initData() {
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            doToast(this, getResources().getString(R.string.isNotNetWork));
            return;
        }
        SharedPreferences sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        token = sp.getString("token", "");
        String url = NetworkUtils.returnUrl() + NetworkUtils.returnSelfTestQuestion() + "?token=" + token + "&vid=" + id;
        Log.d("url ====>>>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int code = response.getInt("code");
                    Log.d("response===>>>", response.toString());
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        exerciseId = data.getString("id");
                        content = data.getString("content");
                        Message message = anserHandle.obtainMessage();
                        message.what = WEB_VIEW;
                        anserHandle.sendMessage(message);
                    } else {
                        doToast(SelfTestQuestionsActivity.this, getResources().getString(R.string.sever_busy));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                doToast(SelfTestQuestionsActivity.this, getResources().getString(R.string.sever_busy));
                return;
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler anserHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WEB_VIEW:
                    tv_current.setText("" + current_count);
                    tv_choose_a.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_grey));
                    tv_choose_b.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_grey));
                    tv_choose_c.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_grey));
                    tv_choose_d.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_grey));
                    WebSettings settings = wb_webView.getSettings();
                    settings.setJavaScriptEnabled(true);
                    settings.setDefaultTextEncodingName("UTF -8");
                    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    wb_webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    wb_webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                    start_time = System.currentTimeMillis();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_choose_a:
                current_count = current_count + 1;
                tv_choose_a.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_blue));
                end_time = System.currentTimeMillis();
                millis_time = end_time - start_time;
                time = millis_time / 1000;
                Log.d("time==>>>", millis_time + ":" + time);
                answer = "A";
                doPostAndGetQuestion(id, exerciseId, answer, time, state, token);
                Log.d("current_count===>>>", "" + current_count);
                break;
            case R.id.tv_choose_b:
                current_count = current_count + 1;
                tv_choose_b.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_blue));
                end_time = System.currentTimeMillis();
                millis_time = end_time - start_time;
                time = millis_time / 1000;
                Log.d("time==>>>", millis_time + ":" + time);
                answer = "B";
                doPostAndGetQuestion(id, exerciseId, answer, time, state, token);
                Log.d("current_count===>>>", "" + current_count);
                break;
            case R.id.tv_choose_c:
                current_count = current_count + 1;
                tv_choose_c.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_blue));
                end_time = System.currentTimeMillis();
                millis_time = end_time - start_time;
                time = millis_time / 1000;
                Log.d("time==>>>", millis_time + ":" + time);
                answer = "C";
                doPostAndGetQuestion(id, exerciseId, answer, time, state, token);
                Log.d("current_count===>>>", "" + current_count);

                break;
            case R.id.tv_choose_d:
                current_count = current_count + 1;
                tv_choose_d.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_blue));
                end_time = System.currentTimeMillis();
                millis_time = end_time - start_time;
                time = millis_time / 1000;
                Log.d("time==>>>", millis_time + ":" + time);
                answer = "D";
                Log.d("current_count===>>>", "" + current_count);
                doPostAndGetQuestion(id, exerciseId, answer, time, state, token);
                break;
        }
    }

    private void doEndGet(final int id) {
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            doToast(this, getResources().getString(R.string.isNotNetWork));
            return;
        }
        String url2 = NetworkUtils.returnUrl() + NetworkUtils.returnSelfTestEnd() + "?vid=" + id;
        Log.d("url == >", url2);
        AsyncHttpClient endClient = new AsyncHttpClient();
        endClient.get(url2, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Intent sIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", id);
                        sIntent.putExtras(bundle);
                        sIntent.setClass(SelfTestQuestionsActivity.this, TestEndActivity.class);
                        startActivity(sIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                doToast(SelfTestQuestionsActivity.this, getResources().getString(R.string.sever_busy));
                return;
            }
        });
    }

    private void doPostAndGetQuestion(final int id, String eId, String answer, long time, int state, String token) {
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            doToast(this, getResources().getString(R.string.isNotNetWork));
            return;
        }
        String url = NetworkUtils.returnUrl() + NetworkUtils.returnSelfTestQuestion() + "?token=" + token + "&vid=" + id;
        Log.d("url == >", url);
        RequestParams params = new RequestParams();
        params.put("id", eId);
        params.put("step", answer);
        params.put("times", time);
        params.put("state", state);
        AsyncHttpClient postClient = new AsyncHttpClient();
        postClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int code = response.getInt("code");
                    Log.d("response===>>>", response.toString());
                    if (code == 0) {
                        Object data1 = response.get("data");
                        if (data1 instanceof JSONArray) {
                            JSONArray data2 = response.getJSONArray("data");
                            if (data2.length() == 0) {
                                doEndGet(id);
                            } else {
                                doToast(SelfTestQuestionsActivity.this, getResources().getString(R.string.sever_busy));
                                return;
                            }

                        } else {
                            JSONObject data = response.getJSONObject("data");
                            exerciseId = data.getString("id");
                            content = data.getString("content");
                            Message message = anserHandle.obtainMessage();
                            message.what = WEB_VIEW;
                            anserHandle.sendMessage(message);
                        }
                    } else {
                        doToast(SelfTestQuestionsActivity.this, getResources().getString(R.string.sever_busy));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                doToast(SelfTestQuestionsActivity.this, getResources().getString(R.string.sever_busy));
                return;
            }
        });

    }
}
