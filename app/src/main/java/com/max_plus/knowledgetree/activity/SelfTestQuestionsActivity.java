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
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.max_plus.knowledgetree.tools.AllToast.doToast;

public class SelfTestQuestionsActivity extends Activity implements View.OnClickListener {
    private int id, count;
    private String exerciseId, content;
    private ImageView iv_back;
    private WebView wb_webView;
    private TextView tv_current, tv_total;
    private final int WEB_VIEW = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_test_questions);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        wb_webView = findViewById(R.id.wb_web);
        tv_current = findViewById(R.id.tv_current);
        tv_total = findViewById(R.id.tv_total);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
        count = bundle.getInt("count");
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
        String token = sp.getString("token", "");
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
                    WebSettings settings = wb_webView.getSettings();
                    settings.setJavaScriptEnabled(true);
                    settings.setDefaultTextEncodingName("UTF -8");
                    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    wb_webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    wb_webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
