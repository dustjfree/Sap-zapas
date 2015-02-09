package com.dustjfree.sap;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class YuristsAct extends ActionBarActivity {

    Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yurists);
        toolbar = (Toolbar)findViewById(R.id.lawToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		String[] web = {
			    "ФЫЫ",
				"ВАП",
				"ПРО"
				} ;
		Integer[] imageId = {
			      R.drawable.lawer_ivanov,
			      R.drawable.lawer_kirilov,
			      R.drawable.lawer_prohorova
			  };
		
		YuristA sad = new YuristA(this, web, imageId);
		ListView lv = (ListView) findViewById(R.id.lvYur);
		lv.setAdapter(sad);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        finish();

        return super.onOptionsItemSelected(item);
    }




}
