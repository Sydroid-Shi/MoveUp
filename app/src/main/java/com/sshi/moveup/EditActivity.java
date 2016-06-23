package com.sshi.moveup;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sshi.moveup.dbcontroler.DatabaseController;
import com.sshi.moveup.dbcontroler.TableControler;
import com.sshi.moveup.util.StringUtil;

import java.util.List;

public class EditActivity extends AppCompatActivity {

    private Button mFinishBtn;
    private Button mCancelBtn;

    private EditText mTaskEt;
    private EditText mTimesEt;
    private EditText mTimeEt;
    private EditText mNoteEt;

    private String tableName;
    private String taskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        tableName = intent.getStringExtra("tableName");
        taskName = intent.getStringExtra("taskName");

        initView();
    }

    private void initView(){

        mTaskEt = (EditText)findViewById(R.id.editview_taskname_et);
        mTimesEt = (EditText)findViewById(R.id.editview_times_et);
        mTimeEt = (EditText)findViewById(R.id.editview_time_et);
        mNoteEt = (EditText)findViewById(R.id.editview_note_et);

        mFinishBtn = (Button)findViewById(R.id.editview_finish_btn);
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = mTaskEt.getText().toString();
                String times = mTaskEt.getText().toString();
                String time = mTaskEt.getText().toString();
                String note = mTaskEt.getText().toString();
                times = (times==null) ? "0" : times;
                time = (time==null) ? "0 s" : time;
                Log.i("sydroid-info",taskName);
                if(StringUtil.isEmpty(taskName)){
                    Toast toast = Toast.makeText(EditActivity.this,"任务名不能为空!",Toast.LENGTH_SHORT);//Task name should not be empty.
                    toast.show();
                    Log.d("sydroid-debug","Task name should not be empty in EditActivity.");
                }else{

                    //TODO Match the taskName.trim() with database, if the same, cannot finish.

                    Log.d("sydroid-debug","Task data will be written into database in EditActivity.");
                    //TODO Write the data to database
//                    TableControler tc = new TableControler();
                    SQLiteDatabase db = openOrCreateDatabase("move_up.db",Context.MODE_PRIVATE,null);
                    ContentValues cv = new ContentValues();
                    cv.put("_task", taskName);
                    cv.put("_default_times", times);
                    cv.put("_default_time", time);
                    cv.put("_note", note);
                    Log.d("sydroid-debug","table name: " + tableName);
                    DatabaseController.insert(db,tableName,cv);

                    //Return to the MainActivity, should not be back by click "Back".
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    intent.putExtra("tableName", tableName);
                    startActivity(intent);
                    EditActivity.this.finish();
                }

            }
        });
        mCancelBtn = (Button)findViewById(R.id.editview_cancel_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sydroid-debug","Edit task has been canceled in EditActivity.");
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.putExtra("tableName", tableName);
                startActivity(intent);
                EditActivity.this.finish();
            }
        });

        if(taskName != null){
            SQLiteDatabase db = openOrCreateDatabase("move_up.db", Context.MODE_PRIVATE, null);
            List<String> taskNames = DatabaseController.selectItemFromTaskName(db, tableName, taskName);
            mTaskEt.setText(taskNames.get(0));
            mTimeEt.setText(taskNames.get(1));
            mTimesEt.setText(taskNames.get(2));
            mNoteEt.setText(taskNames.get(3));
        }
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
