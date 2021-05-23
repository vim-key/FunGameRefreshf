package com.hitomi.fungamerefreshdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hitomi.refresh.view.FunGameRefreshView;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    private FunGameRefreshView refreshView;

    private ListView listView;

    private List<String> dataList;

    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        refreshView = (FunGameRefreshView) findViewById(R.id.refresh_fun_game);
        refreshView.setLoadingText("玩个游戏解解闷");
        refreshView.setGameOverText("游戏结束");
        refreshView.setLoadingFinishedText("加载完成");
        refreshView.setTopMaskText("下拉刷新");
        refreshView.setBottomMaskText("上下滑动控制游戏");

        listView = (ListView) findViewById(R.id.list_view);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, createDate());

        listView.setAdapter(arrayAdapter);
        refreshView.setOnRefreshListener(new FunGameRefreshView.FunGameRefreshListener() {
            @Override
            public void onRefreshing() {
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dataList.add("X");
            arrayAdapter.notifyDataSetChanged();
        }
    };

    private List<String> createDate() {
        dataList = new ArrayList<>();
        dataList.add("A");
        dataList.add("B");
        dataList.add("C");
        dataList.add("D");
        dataList.add("E");
        dataList.add("F");
        dataList.add("G");
        return dataList;
    }
}
