package com.sshi.moveup.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sshi.moveup.MainActivity;

import java.util.List;

/**
 * Created by sshi on 6/12/2016.
 */
public class MainLVAdapter extends BaseAdapter {
    private Context mCtx;
    private List<String> mList;

    public MainLVAdapter(Context ctx, List<String> list){
        this.mCtx = ctx;
        this.mList = list;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private class Hodler{
        TextView tv_task;
    }
}
