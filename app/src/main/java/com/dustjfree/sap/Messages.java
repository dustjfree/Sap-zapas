package com.dustjfree.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class Messages extends ActionBarActivity{
	
	JSONparser jParser = new JSONparser();
	JSONparser mjsp = new JSONparser();
	String idpsych;
	ListView mes_lv;
    Toolbar toolbar;
	EditText editText;
	ArrayList<HashMap<String, String>> messagesList;
	JSONArray messages = null;
	String TAG_SUCCESS = "success";
	String TAG_MESSAGES = "messages";
	String TAG_TEXT = "mes_text";
	String TAG_FROM = "mes_from";
	String TAG_TO = "mes_to";
	String TAG_DATA = "mes_data";
	String yourid;
    String mes;
	int proof;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		toolbar = (Toolbar)findViewById(R.id.messageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        idpsych = getIntent().getStringExtra("spec_id");
		mes_lv = (ListView)findViewById(R.id.messages_lv);
        mes_lv.setDivider(null);
        mes_lv.setDividerHeight(0);
		editText = (EditText)findViewById(R.id.typeMessageField);
		messagesList = new ArrayList<HashMap<String, String>>();
		SharedPreferences asdPREF = getSharedPreferences("yourID", this.MODE_PRIVATE);
		if(asdPREF.contains("user_id")){
			yourid = asdPREF.getString("user_id", "");
		}
		proof = 0;
        if(jParser.isOnline(this))
		    new LoadAllMessages().execute();
	}
	
	public void Send_message(View view) {
        mes = editText.getText().toString();
        if (jParser.isOnline(Messages.this) && !mes.isEmpty()) {
            new CreateNewMessage().execute();

            editText.setText("");
            recreate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return super.onOptionsItemSelected(item);
    }

	class LoadAllMessages extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Messages.this);
            pDialog.setMessage("Загружаем ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mesfrom", yourid));
            params.add(new BasicNameValuePair("mesto", idpsych));

            JSONObject json = jParser.makeHttpRequest(getString(R.string.url_get_message), "GET", params);
            Log.d("Create Response", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    proof = 1;
                    messages = json.getJSONArray(TAG_MESSAGES);
                    Log.d("Success", " Yeap");

                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject c = messages.getJSONObject(i);

                        String mes_text = c.getString(TAG_TEXT);
                        String mes_from = c.getString("mes_from");
//						String mes_data = c.getString(TAG_DATA);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TAG_TEXT, mes_text);
                        map.put("mes_from", mes_from);

//						map.put(TAG_DATA, mes_data);
                        messagesList.add(map);

                    }
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (proof == 1) {

                Log.d("message list", messagesList.get(0).get(TAG_FROM).toString());

                String[] from = {TAG_TEXT};
                int[] to = {R.id.message_text};
                ListAdapter adapter = new SimpleAdapter(Messages.this, messagesList, R.layout.fragment_messages, from, to) {
                    @Override
                    public View getView(int position, View convertView,
                                        ViewGroup parent) {
                        // TODO Auto-generated method stub
                        View row = super.getView(position, convertView, parent);
                        TextView txt = (TextView) row.findViewById(R.id.message_text);

                        Log.d("Hello", messagesList.get(position).get(TAG_FROM));

                        if (Integer.valueOf(messagesList.get(position).get(TAG_FROM)) == Integer.valueOf(yourid)) {
                            txt.setBackgroundResource(R.drawable.speech_bubble_green);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            txt.setLayoutParams(params);
                        } else {
                            txt.setBackgroundResource(R.drawable.speech_bubble_blue);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            txt.setLayoutParams(params);
                        }
                        return row;
                    }
                };
                mes_lv.setAdapter(adapter);
                mes_lv.setSelection(mes_lv.getAdapter().getCount() - 1);

            }
        }
    }
	
	class CreateNewMessage extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}
		@Override
	    protected void onProgressUpdate(String... values) {
	       Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
	    }

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mes_text", mes));
			params.add(new BasicNameValuePair("mes_from", yourid));
			params.add(new BasicNameValuePair("mes_to", idpsych));
			JSONObject json = mjsp.makeHttpRequest(getString(R.string.url_add_message),"POST", params);
			Log.d("Create Response", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
            mes_lv.setSelection(mes_lv.getAdapter().getCount() - 1);
			
		}
		
		
	}
	
	
}
