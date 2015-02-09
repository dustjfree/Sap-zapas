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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendHist extends ActionBarActivity{

    Toolbar toolbar;
	LayoutInflater inflater;
	ViewGroup container;
	private EditText mEdit;
	
	final String LOG_TAG = "myLogs";
	JSONparser mjsp = new JSONparser();

	private static final String TAG_SUCCESS = "success";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_hist);
		mEdit = (EditText) findViewById(R.id.sendedHist); 
		Button btn = (Button)findViewById(R.id.btnSend);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new CreateNewHistory().execute();

			}
		});
        toolbar = (Toolbar)findViewById(R.id.sendHistToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();

        return super.onOptionsItemSelected(item);
    }

	class CreateNewHistory extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SendHist.this);
            pDialog.setMessage("Загрузка ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
			
		}
		@Override
	    protected void onProgressUpdate(String... values) {
	       Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
	    }

		protected String doInBackground(String... args) {
			String etxt = mEdit.getText().toString();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("history", etxt));
			JSONObject json = mjsp.makeHttpRequest(getString(R.string.url_add_history),"POST", params);
			Log.d("Create Response", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					finish();
				} else {
					// failed to create product
					Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();

		}
		
	}

}
