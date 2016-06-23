package com.sshi.moveup;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sshi.moveup.dbcontroler.DatabaseController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button mBtnTaskAdd;
    private ListView mLvTaskList;
    private final int INTENT_CODE = 1;
    private ContextMenu mPopMenu;

    private String tableName;
    private List<Map<String,String>> mTaskNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        tableName = intent.getStringExtra("tableName");

        initView();

    }

    private void initView(){
        mBtnTaskAdd = (Button)findViewById(R.id.mainview_addtask_btn);
        mBtnTaskAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Use intent to go to the EditActivity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("tableName", tableName);
                startActivityForResult(intent, INTENT_CODE);
                Log.d("intent code: ", INTENT_CODE+"");
                MainActivity.this.finish();//TODO EditActivity should modify the "Back" for intent back here.
            }
        });

        mLvTaskList = (ListView)findViewById(R.id.mainview_projectlist_lv);

        SQLiteDatabase db = openOrCreateDatabase("move_up.db", Context.MODE_PRIVATE,null);

        String[] from = {"task_name"};
        int[] to = {R.id.task_name};
        SimpleAdapter adapter = new SimpleAdapter(mLvTaskList.getContext(), getData(db), R.layout.list_item, from, to);
        mLvTaskList.setAdapter(adapter);

        mLvTaskList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                mPopMenu = menu;
//                menu.setHeaderTitle("菜单");
                menu.add(0,0,0,"开始");//Start
                menu.add(0,1,0,"显示");//Show
                menu.add(0,2,0,"修改");//Edit
                menu.add(0,3,0,"删除");//Delete

            }
        });
//        registerForContextMenu(mLvTaskList);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int clickListIndex = minfo.position;
        Log.d("sydroid-debug","The position of menu: " + clickListIndex);
        Map<String,String> map = mTaskNames.get(clickListIndex);
        String taskName = map.get("task_name");
        Log.d("sydroid-debug","taskName: " + taskName);

        if(item.getItemId() == 0) {
            Log.d("sydroid-debug","toast show.");
            Intent intent = new Intent(MainActivity.this, ActiveTaskActivity.class);
            intent.putExtra("tableName", tableName);
            intent.putExtra("taskName",taskName);
            startActivity(intent);
            MainActivity.this.finish();
        }else if(item.getItemId() == 1) {
            showToast("Show");
            Intent intent = new Intent(MainActivity.this, TaskChartActivity.class);
            intent.putExtra("tableName", tableName);
            startActivity(intent);
            MainActivity.this.finish();
        }else if(item.getItemId() == 2) {
            showToast("Edit");
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("tableName", tableName);
            intent.putExtra("taskName", taskName);
            startActivity(intent);
            MainActivity.this.finish();
        }else if(item.getItemId() == 3) {
            showToast("Delete");
            //get task name from the item, and delete the data related in db.
            SQLiteDatabase db = openOrCreateDatabase("move_up.db", Context.MODE_PRIVATE,null);
            DatabaseController.delete(db, tableName, taskName);
        }
        return false;
    }

    private List<Map<String,String>> getData(SQLiteDatabase db){
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        List<String> taskNames = DatabaseController.selectTaskNames(db, tableName);
        if(taskNames == null){
            return null;
        }
        for(String taskName : taskNames) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("task_name", taskName);
            list.add(map);
        }
        db.close();
        mTaskNames = list;
        return list;
    }

    private void showToast(String info){
        Toast toast = Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == INTENT_CODE){//update the listview
            initView();
        }
    }

}
