package com.max_plus.knowledgetree.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.max_plus.knowledgetree.R;

/**
 * Created by djz on 2017/10/17.
 */

public class TakePhotoPopWin extends PopupWindow {
    private Context mContext;

    private View view;

    private TextView take_phone, choose_album, choose_system;

    public TakePhotoPopWin(Context mContext, View.OnClickListener itemsOnClick) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.take_photo_pop, null);
        take_phone = view.findViewById(R.id.take_phone);
        take_phone.setOnClickListener(itemsOnClick);

        choose_album = view.findViewById(R.id.choose_album);
        choose_album.setOnClickListener(itemsOnClick);

        choose_system = view.findViewById(R.id.choose_system);
        choose_system.setOnClickListener(itemsOnClick);

        this.setOutsideTouchable(true); // 设置外部可点击


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);


        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener()

        {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.pop_layout).getTop();
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
