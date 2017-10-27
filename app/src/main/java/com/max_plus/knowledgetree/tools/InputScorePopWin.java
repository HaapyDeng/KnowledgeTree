package com.max_plus.knowledgetree.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.max_plus.knowledgetree.R;

/**
 * Created by Administrator on 2017/10/27.
 */

public class InputScorePopWin extends PopupWindow {
    private TextView tv_characters, tv_confirm;
    private EditText et_score;
    private View mMenuView;
    public static String score;


    public InputScorePopWin(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.input_score_pop, null);

        tv_characters = mMenuView.findViewById(R.id.tv_characters);

        tv_confirm = mMenuView.findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(itemsOnClick);

        et_score = mMenuView.findViewById(R.id.et_score);
        et_score.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                score = et_score.getText().toString().trim();
                int len = score.length();
                if (len == 0) {
                    tv_characters.setText(R.string.three_characters);
                } else if (len == 1) {
                    tv_characters.setText(R.string.two_characters);
                } else if (len == 2) {
                    tv_characters.setText(R.string.one_characters);
                } else {
                    tv_characters.setText(R.string.zero_characters);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.take_photo_anim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.score_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
