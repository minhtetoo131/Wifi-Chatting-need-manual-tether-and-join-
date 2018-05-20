package com.minhtetoo.wifichatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Myadapter extends BaseAdapter {

    LayoutInflater mLayoutInflater;
    ArrayList<GetModel> mData;

    public Myadapter(Context context,ArrayList<GetModel> data){
        mData = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = null;
        TextView textView = null;
        if (mData.get(i).isMeOrU){
            itemView = mLayoutInflater.inflate(R.layout.row_me,null);
            textView = itemView.findViewById(R.id.tv_me);
        }else{
            itemView = mLayoutInflater.inflate(R.layout.row_other,null);
            textView = itemView.findViewById(R.id.tv_other);
        }
        textView.setText(mData.get(i).getText());
        return itemView;
    }

    public void setNewItems(ArrayList<GetModel> newData){
        mData = newData;
        notifyDataSetChanged();
    }
}
