package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.AllToast;
import com.max_plus.knowledgetree.tools.CacheDataManager;
import com.max_plus.knowledgetree.tools.MyWarningDailog;

public class SetActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_about, tv_check_update, tv_clear_cache, tv_cache;
    private Button btn_login_out;
    private String cache;
    private LinearLayout ll_cache;
    private Dialog clearCacheDialog, clearOverCacheDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        try {
            cache = CacheDataManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        tv_cache.setText(cache);

        ll_cache = findViewById(R.id.ll_cache);
        ll_cache.setOnClickListener(this);

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
                Log.d("cache...===>>", cache);
                if (cache.equals("0.0B")) {
                    AllToast.doToast(this, getString(R.string.no_cache));
                    return;
                }
                clearCacheDialog = new MyWarningDailog(SetActivity.this, R.layout.clearing_dialog_layout, null);
                clearCacheDialog.show();
                new Thread(new clearCache()).start();
                break;
            case R.id.btn_login_out:
                doLoginOut();
                break;
        }
    }

    class clearCache implements Runnable {

        @Override

        public void run() {

            try {

                CacheDataManager.clearAllCache(SetActivity.this);
                cache = "0.0B";
                Thread.sleep(3000);

                if (CacheDataManager.getTotalCacheSize(SetActivity.this).startsWith("0")) {
                    handler.sendEmptyMessage(0);
                }

            } catch (Exception e) {

                return;

            }

        }

    }

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {

                case 0:
//                    Toast.makeText(SetActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
                    clearCacheDialog.dismiss();
                    try {

                        tv_cache = findViewById(R.id.tv_cache);
                        tv_cache.setText(CacheDataManager.getTotalCacheSize(SetActivity.this));

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
            }
        }

        ;
    };


    private void doLoginOut() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
