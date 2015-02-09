package com.dustjfree.sap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class YuristA extends ArrayAdapter<String>{
	private final Context context;
	private final String[] web;
	private final Integer[] imageId;
	public YuristA(Context context,
		String[] web, Integer[] imageId) {
			super(context, R.layout.yurist_item, web);
			this.context = context;
			this.web = web;
			this.imageId = imageId;
			}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	View rowView= inflater.inflate(R.layout.yurist_item, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.txtYur);
	ImageView imageView = (ImageView) rowView.findViewById(R.id.imgYur);
	txtTitle.setText(web[position]);
	imageView.setImageResource(imageId[position]);
	return rowView;
	}
	}
