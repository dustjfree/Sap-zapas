package com.dustjfree.sap;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Rega extends Activity {
	JSONparser jParser = new JSONparser();
	private static final String TAG_SUCCESS = "success";
	public static final String TAG_PREF = "yourID";
	public static final String TAG_USER_ID = "user_id";
	EditText loginInput;
	EditText passInput;
	int proof;
	String user_id;
    //String loadintText = getString(R.string.loading_text);
	SharedPreferences sharedUserID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registr);
		loginInput = (EditText)findViewById(R.id.rLoginInput);
		passInput = (EditText)findViewById(R.id.rPassInput);
		proof=0;
		sharedUserID = getSharedPreferences(TAG_PREF, this.MODE_PRIVATE);
		Button btn = (Button)findViewById(R.id.buttonRega);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((loginInput.getText().length()>0) && (passInput.getText().length()>0)){
                    try{
					new UserLogin().execute();
                    } catch (Exception e) {
                        Log.d("dasd", "ERROR");
                    }
				} else {
					Toast.makeText(Rega.this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	public void butt_registr(View view){
		Intent intent = new Intent(Rega.this, RegisrtationActivity.class);
		startActivity(intent);
	}
	
	class UserLogin extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Rega.this);
            pDialog.setMessage("Загружаем...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
		}

		protected String doInBackground(String... args) {
			String usrLogin = loginInput.getText().toString();
			String usrPass = passInput.getText().toString();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_login", usrLogin));
			params.add(new BasicNameValuePair("user_pass", usrPass));
			JSONObject json = jParser.makeHttpRequest(getString(R.string.url_usr_loginn),"POST", params);
			Log.d("Create Response", json.toString());
			try {
				user_id = json.getString(TAG_USER_ID);
				int success = json.getInt(TAG_SUCCESS);
				switch(success){
				case 1: proof = 1;
						break;
				case 2: proof = 2;
						break;
				case 0: break;
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			Editor sedit = sharedUserID.edit();
			sedit.putString(TAG_USER_ID, user_id);
			sedit.apply();
			switch(proof){
			case 0: Toast.makeText(Rega.this, R.string.invalid_entity, Toast.LENGTH_SHORT).show();
					break;
			case 1: Intent intent = new Intent(Rega.this,Histories.class);
					startActivity(intent);
					finish();
					break;
			}
		}
	}

}
