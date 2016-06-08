package com.sshi.moveup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button mBtnTaskAdd;
    private ListView mLvTaskList;
    private final int INTENT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();






        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void initView(){
        mBtnTaskAdd = (Button)findViewById(R.id.mainview_addtask_btn);
        mBtnTaskAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Use intent to go to the EditActivity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, INTENT_CODE);
                Log.d("intent code: ", INTENT_CODE+"");
            }
        });

        mLvTaskList = (ListView)findViewById(R.id.mainview_projectlist_lv);
        Map<String, String> map = new HashMap<String, String>();
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        String[] from = {"task_name"};
        int[] to = {R.id.task_name};
        map.put(from[0], "Running");// get data from db
        SimpleAdapter adapter = new SimpleAdapter(mLvTaskList.getContext(), list, R.layout.list_item, from, to);
        mLvTaskList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == INTENT_CODE){//update the listview

        }
    }

}
