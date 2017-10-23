package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.max_plus.knowledgetree.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemPictureActivity extends Activity {
    private GridView systemGridView;
    private ImageView iv_back;
    private int[] pictures = new int[]{R.drawable.tabbar_ic_my_pressed, R.drawable.tabbar_ic_my_pressed, R.drawable.tabbar_ic_my_pressed,
            R.drawable.tabbar_ic_my_pressed, R.drawable.tabbar_ic_my_pressed, R.drawable.tabbar_ic_my_pressed
            , R.drawable.tabbar_ic_my_pressed, R.drawable.tabbar_ic_my_pressed, R.drawable.tabbar_ic_my_pressed};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_picture);
        initView();
        SimpleAdapter adapter = new SimpleAdapter(this, getList(pictures), R.layout.gridview_item, new String[]{"picture", "pictureid"}, new int[]{R.id.system_img_item});
        systemGridView.setAdapter(adapter);
        systemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("你点击了：", "" + position + ":" + id + ":" + view.toString());

            }
        });
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        systemGridView = findViewById(R.id.gv_1);
    }

    public List<Map<String, Object>> getList(int[] pictures) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 0; i < pictures.length; i++) {
            map = new HashMap<String, Object>();
            map.put("picture", pictures[i]);
            map.put("pictureid", i);
            list.add(map);
        }
        Log.d("this is getList", list.size() + "");
        return list;
    }
}
