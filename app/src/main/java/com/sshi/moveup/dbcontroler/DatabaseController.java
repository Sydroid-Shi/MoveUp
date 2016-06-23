package com.sshi.moveup.dbcontroler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sshi.moveup.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sshi on 6/12/2016.
 */
public class DatabaseController {

    private String CREATE_TABLE = "";
    public static void createUserTable(SQLiteDatabase db, String tableName, String tableName2){
        String cmd_create_table = "create table if not exists " + tableName + "(_id integer primary key autoincrement," +
                "_task text, _default_time text, _default_times text, _note text)";
        db.execSQL(cmd_create_table);
        if(!StringUtil.isEmpty(tableName2)){
            String cmd_create_tasks_table = "create table if not exists " + tableName2 + "(_id integer primary key autoincrement," +
                    "_task text, _date text, _time text, _times text)";
            db.execSQL(cmd_create_tasks_table);
        }
        db.close();
    }

    public static void createTasksTable(SQLiteDatabase db, String tableName){
        String cmd_create_tasks_table = "create table if not exists " + tableName + "(_id integer primary key autoincrement," +
                "_task text, _date text, _time text, _times text)";
        db.execSQL(cmd_create_tasks_table);
        db.close();
    }

    public static void insert(SQLiteDatabase db, String tableName,ContentValues values){
        db.insert(tableName,null,values);
        db.close();
    }

    public static void delete(SQLiteDatabase db, String tableName,String itemName){
        db.delete(tableName, "_task = ?", new String[] {itemName});
        db.close();
    }

    public static void modify(String tableName,ContentValues values){

    }

    public static List<String> selectTaskNames(SQLiteDatabase db, String tableName){
        List<String> taskNames = new ArrayList<String>();
        Cursor cursor = db.query(tableName, new String[] {"_task"}, null, null,null,null,null);
        while(cursor.moveToNext()){
            taskNames.add(cursor.getString(cursor.getColumnIndex("_task")));
        }
        return taskNames;
    }

    public static List<String> selectItemFromTaskName(SQLiteDatabase db, String tableName, String itemName){
        List<String> list = new ArrayList<String>();
        Cursor cursor = db.query(tableName, null, "_task=?", new String[]{itemName}, null, null, null);
        Log.i("sydroid-info","The columns count: " + cursor.getColumnCount() + " | The rows count: " + cursor.getCount());
        int index=0;
        if(cursor.moveToFirst()) {
            while (index < cursor.getColumnCount()) {//Just one row, cursor index is from -1, should not always move to next.
                list.add(cursor.getString(index));
                Log.d("sydroid-debug", "The columns are: " + cursor.getString(index));
                index++;
            }
        }
        db.close();
        return list;
    }

}
