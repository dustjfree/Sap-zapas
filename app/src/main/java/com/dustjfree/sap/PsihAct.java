package com.dustjfree.sap;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PsihAct extends ActionBarActivity {
	JSONparser jParser = new JSONparser();
	ListView lv;
	ArrayList<HashMap<String, String>> PsychList;
	JSONArray psych_name = null;
    Toolbar toolbar;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PSYCH_DATA = "psych_name";
	private static final String TAG_PSYCH_ID = "user_id";
	private static final String TAG_PSYCH = "psych";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.psihol);
        toolbar = (Toolbar)findViewById(R.id.psychToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		PsychList = new ArrayList<HashMap<String, String>>();
		lv = (ListView)findViewById(R.id.lvPsih);
		new LoadAllPsych().execute();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item){

        finish();

        return super.onOptionsItemSelected(item);
    }

	class LoadAllPsych extends AsyncTask<String, String, String>{
		private ProgressDialog pDialog;
	      @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            
	            pDialog = new ProgressDialog(PsihAct.this);
	            pDialog.setMessage("Загружаем ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	      }
	      
		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(getString(R.string.url_get_psych_list), "GET", params);
			
			// Check your log cat for JSON reponse
			//Log.d("All Histories ", json.toString());
			
			Log.d("Create Response", json.toString());
			
			try {
				// Checking for SUCCESS TAG
				
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					psych_name = json.getJSONArray(TAG_PSYCH);
					Log.d("Success", " Yeap");
					
					// looping through All Products
					for (int i = 0; i < psych_name.length(); i++) {
						JSONObject c = psych_name.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_PSYCH_ID);
						String name = c.getString(TAG_PSYCH_DATA);
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_PSYCH_ID, id);
						map.put(TAG_PSYCH_DATA, name);
						// adding HashList to ArrayList
						PsychList.add(map);
						
					}
				} else {
					// no products found
					// Launch Add New product Activity
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
		
	protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			String[] from = {TAG_PSYCH_DATA};
			int[] to = {R.id.txtPsih};
			ListAdapter adapter = new SimpleAdapter(PsihAct.this, PsychList, R.layout.psihol_item, from,to);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Log.d("psychID", PsychList.get(arg2).get(TAG_PSYCH_ID).toString());
					Intent intent = new Intent(PsihAct.this, Messages.class);
					intent.putExtra("psych_id", PsychList.get(arg2).get(TAG_PSYCH_ID).toString());
					startActivity(intent);
				}
			});
				
		}
	}

}
