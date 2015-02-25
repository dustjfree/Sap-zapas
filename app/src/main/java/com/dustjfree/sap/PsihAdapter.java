package com.dustjfree.sap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class PsihAdapter extends ArrayAdapter<HashMap<String,String>>{
	private final Context context;
	Bitmap image;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
	public PsihAdapter(Context context,ArrayList<HashMap<String,String>> _list
            , Bitmap _image) {
			super(context, R.layout.psihol_item, _list);
			this.context = context;
			list = _list;
			image = _image;
			}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	View rowView= inflater.inflate(R.layout.psihol_item, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.txtPsih);
	ImageView imageView = (ImageView) rowView.findViewById(R.id.imgPsih);
	imageView.setImageBitmap(image);
	return rowView;
	}
	}