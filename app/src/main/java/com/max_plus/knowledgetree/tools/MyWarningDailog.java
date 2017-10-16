package com.max_plus.knowledgetree.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.max_plus.knowledgetree.R;

/**
 * Created by djz on 2017/10/16.
 */

public class MyWarningDailog extends Dialog {
    private Context context;      // 上下文
    private int layoutResID;      // 布局文件id
    private int[] listenedItems;

    public MyWarningDailog(Context context, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        setContentView(layoutResID);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 1 / 3; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失
    }
}
