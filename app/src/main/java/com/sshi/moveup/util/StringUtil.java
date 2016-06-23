package com.sshi.moveup.util;

import android.util.Log;

/**
 * Created by sshi on 6/9/2016.
 */
public class StringUtil {

    public static boolean isEmpty(String str){
        if(str == null){
            return true;
        }else if(str.trim().equals("")){
            return true;
        }
        return false;
    }

    public static String changeEmailToTableName(String email){
        String tableName = email;
        tableName = tableName.replace("@","_");
        tableName = tableName.replace("-","_");
        tableName = tableName.replace(".","_");
        Log.d("sydroid-debug","[StringUtil_changeEmailToTableName] The table name is: " + tableName);
        return tableName;
    }
}
