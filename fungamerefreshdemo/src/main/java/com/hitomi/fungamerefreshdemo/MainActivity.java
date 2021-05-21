package com.hitomi.fungamerefreshdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnListView, btnGridView, btnRecycleView, btnViewGroup, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setViewListener();
    }

    private void initView() {
        btnListView = (Button) findViewById(R.id.btn_list_view);
        btnGridView = (Button) findViewById(R.id.btn_grid_view);
        btnRecycleView = (Button) findViewById(R.id.btn_recycle_view);
        btnViewGroup = (Button) findViewById(R.id.btn_viewgroup);
        btnView = (Button) findViewById(R.id.btn_view);
    }

    private void setViewListener() {
        btnListView.setOnClickListener(this);
        btnGridView.setOnClickListener(this);
        btnRecycleView.setOnClickListener(this);
        btnViewGroup.setOnClickListener(this);
        btnView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_list_view:
                startActivity(new Intent(this, ListViewActivity.class));
                break;
            case R.id.btn_grid_view:
                break;
            case R.id.btn_recycle_view:
                break;
            case R.id.btn_viewgroup:
                break;
            case R.id.btn_view:
                break;
        }
    }
}
