package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.adapter.TestSelfViewPagerAdapter;
import com.max_plus.knowledgetree.tools.AllToast;

import java.util.ArrayList;
import java.util.List;

public class GoodTestActivity extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private TestSelfViewPagerAdapter vpAdapter;
    private List<View> views;

    // 底部小点图片
    private ImageView[] dots;
    private ImageView iv_back;
    private Button start_test;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_test);
        // 初始化页面
        initViews();
        // 初始化底部小点
        initDots();
    }

    View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.start_test:
                    Intent intent = new Intent();
                    intent.setClass(GoodTestActivity.this, TestSettingActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void initViews() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(myClickListener);

        start_test = findViewById(R.id.start_test);
        start_test.setOnClickListener(myClickListener);

        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.good_test_one_layout, null));
        views.add(inflater.inflate(R.layout.good_test_two_layout, null));
        views.add(inflater.inflate(R.layout.good_test_three_layout, null));

        // 初始化Adapter
        vpAdapter = new TestSelfViewPagerAdapter(views, this);
        vp = findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        vp.setOffscreenPageLimit(views.size());// 加载缓存的页面个数
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态
        setCurrentDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
