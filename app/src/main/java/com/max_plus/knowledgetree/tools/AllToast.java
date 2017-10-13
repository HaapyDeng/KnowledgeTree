package com.max_plus.knowledgetree.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/10/13.
 */

public class AllToast {
    public static void doToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
