package com.sshi.moveup.util;

/**
 * Created by sshi on 6/9/2016.
 */
public class StringUtil {

    public static boolean isEmpty(String str){
        if(str == null){
            return true;
        }else if(str.trim() == ""){
            return true;
        }
        return false;
    }
}
