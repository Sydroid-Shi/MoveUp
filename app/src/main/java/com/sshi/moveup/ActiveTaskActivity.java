package com.sshi.moveup;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import com.sshi.moveup.dbcontroler.DatabaseController;
import com.sshi.moveup.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActiveTaskActivity extends AppCompatActivity {

    private Button mClickBtn;
    private EditText mTimesEt;
    private Chronometer mTimer;
    private boolean isStart = false;
    private String taskName;
    private String tableName;

    private TextView mTitleTv;
    private Button mBackBtn;
    private Button mFinishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_task);
        Intent intent = getIntent();
        taskName = intent.getStringExtra("taskName");
        tableName = intent.getStringExtra("tableName");
        initBarView();
        initView();
    }

    private void initView(){
        mTimer = (Chronometer)findViewById(R.id.activetaskview_chronometer);

        mTimesEt = (EditText)findViewById(R.id.activetaskview_times_et);

        mClickBtn = (Button)findViewById(R.id.activetaskview_startorfinish_btn);
        mClickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String times = mTimesEt.getText().toString();
                if(isStart){
                    isStart = false;
                    mClickBtn.setText("开始");
                    mClickBtn.setBackgroundColor(Color.GREEN);
                    if(StringUtil.isEmpty(times)){
                        times = "0";
                    }
                    mTimer.stop();
                    Log.d("sydroid-debug","Click stop in ActiveTaskActivity.");
                }else{
                    isStart = true;
                    mClickBtn.setText("结束");
                    mClickBtn.setBackgroundColor(Color.RED);
                    mTimer.start();
                    Log.d("sydroid-debug","Click start in ActiveTaskActivity.");
                }
            }
        });

    }

    private void initBarView(){
        mBackBtn = (Button)findViewById(R.id.activetaskview_back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sydroid-info","Click back button in ActiveTaskActivity.");
                intentToMainActivity();
            }
        });

        mFinishBtn = (Button)findViewById(R.id.activetaskview_finish_btn);
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sydroid-info","Click finish button in ActiveTaskActivity.");
                String time = mTimer.getText().toString();
                String times = mTimesEt.getText().toString();
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd");
                String today = format.format(date);

                SQLiteDatabase db = openOrCreateDatabase("move_up.db", Context.MODE_PRIVATE,null);
                ContentValues cv = new ContentValues();
                cv.put("_task",taskName);
                cv.put("_date", today);
                cv.put("_time", time);
                cv.put("_times", times);
                DatabaseController.insert(db, tableName+"_tasks", cv);

                intentToMainActivity();
            }
        });

        mTitleTv = (TextView)findViewById(R.id.activetaskview_title_tv);
        Log.d("sydroid-debug","taskName in ActiveTaskActivity: " + taskName);
        if(!StringUtil.isEmpty(taskName)) {
            mTitleTv.setText(taskName);
            mTitleTv.setTextColor(Color.WHITE);
        }
    }

    private void intentToMainActivity(){
        Intent intent = new Intent(ActiveTaskActivity.this, MainActivity.class);
        intent.putExtra("tableName",tableName);
        startActivity(intent);
        ActiveTaskActivity.this.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(this.getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
        }
        return super.onTouchEvent(event);
    }
}
