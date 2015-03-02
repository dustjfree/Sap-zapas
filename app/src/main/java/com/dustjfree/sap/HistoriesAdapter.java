package com.dustjfree.sap;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Пользователь on 19.01.2015.
 */
public class HistoriesAdapter extends ArrayAdapter<HashMap<String, String>> {

    private Context context;
    ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();

    public HistoriesAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        super(context, R.layout.hist_item,list);
        this.context = context;
        mList = list;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.hist_item,null,true);
        TextView tvHist = (TextView) rowView.findViewById(R.id.historyText);


        TextView tvCommCount = (TextView)rowView.findViewById(R.id.commentsCountTxt);
        HashMap<String, String> map = new HashMap<String, String>();

        map = mList.get(position);



        tvHist.setText(map.get("history"));
        tvCommCount.setText(map.get("comments"));
        return rowView;
    }
}
