package com.dustjfree.sap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		SharedPreferences asdPREF = getSharedPreferences("yourID", this.MODE_PRIVATE);
		if(asdPREF.contains("user_id")){
			Log.d("USERID", asdPREF.getString("user_id", ""));
		}
		
		String[] web = {
			    "Анонимные истории",
			      "Консультация психолога",
			      "Консультация юриста"
			  } ;
		Integer[] imageId = {
			      R.drawable.sett_newsletter,
			      R.drawable.sett_psih,
			      R.drawable.sett_yurik
			  };
		SettingAdapter sad = new SettingAdapter(Settings.this, web, imageId);
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(sad);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(position == 0) {
					Intent intent = new Intent(Settings.this, Histories.class);
					startActivity(intent);
				}
				if(position == 1) {
					Intent intent = new Intent(Settings.this, PsihAct.class);
					startActivity(intent);
				}
				if (position == 2) {
					Intent intent = new Intent(Settings.this, YuristsAct.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
