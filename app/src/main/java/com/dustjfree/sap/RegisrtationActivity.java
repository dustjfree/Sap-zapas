package com.dustjfree.sap;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisrtationActivity extends ActionBarActivity{
	JSONparser jParser = new JSONparser();
    Toolbar toolbar;
	EditText edLogin;
	EditText edPass;
    EditText edRePass;
    EditText edSecretKey;
    String login, pass, rePass, secretKey;
    TextView alertLogin, alertPass, alertRePass, alertSecretKey;
	int proof;
    SharedPreferences sharePrefs;
	private static final String TAG_SUCCESS = "success";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regisrtation);

        sharePrefs = getSharedPreferences("yourID", this.MODE_PRIVATE);

        toolbar = (Toolbar)findViewById(R.id.RegToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		edLogin = (EditText) findViewById(R.id.edit_reg_login);
		edPass = (EditText) findViewById(R.id.edit_reg_pass);
		edRePass = (EditText)findViewById(R.id.edit_reg_pass_re);
        edSecretKey = (EditText)findViewById(R.id.edit_reg_secret_key);
        alertLogin = (TextView)findViewById(R.id.err_login_message);
        alertPass = (TextView)findViewById(R.id.err_pass_message);
        alertRePass = (TextView)findViewById(R.id.err_accept_pass_message);
        alertSecretKey  = (TextView)findViewById(R.id.err_secret_key_message);

	}

	public void RegOnClick(View view) {
        login = edLogin.getText().toString();
        pass = edPass.getText().toString();
        rePass = edRePass.getText().toString();
        secretKey = edSecretKey.getText().toString();
        if (login.isEmpty() || pass.isEmpty() || rePass.isEmpty() || secretKey.isEmpty())
            Toast.makeText(this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
        else {
            if (pass.matches(rePass))
                new CreateNewUser().execute();
            else
                alertRePass.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();

        return super.onOptionsItemSelected(item);
    }

	class CreateNewUser extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisrtationActivity.this);
            pDialog.setMessage("Загружаем ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
			proof = 1;
		}
		@Override
	    protected void onProgressUpdate(String... values) {
	       Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
	    }

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_login", login));
			params.add(new BasicNameValuePair("user_pass", pass));
            params.add(new BasicNameValuePair("school_key", secretKey));
			JSONObject json = jParser.makeHttpRequest(getString(R.string.url_reg_user),"POST", params);
			Log.d("Create Response", json.toString());
			try {
				final int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					finish();
				} else {
					proof = 0;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if(proof == 0){

				alertLogin.setVisibility(View.VISIBLE);
			}
            SharedPreferences.Editor ed = sharePrefs.edit();
            ed.putString("school_key", secretKey);
            ed.apply();
		}
	}
}
