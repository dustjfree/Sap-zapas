package com.dustjfree.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.widget.Toast;

public class Comments extends ActionBarActivity {


	JSONparser mjsp = new JSONparser();
	ArrayList<HashMap<String, String>> commentList;
	JSONparser jParser = new JSONparser();

    Toolbar toolbar;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_DATE = "commDate";
	private static final String TAG_COMMENT = "comment";
    SharedPreferences sharedUserID;
	EditText editText;
	int id;
	String idstr;
	ArrayAdapter<String> aadapter;
	JSONArray comments = null;
	ListView listView;
    String userID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        listView = (ListView) findViewById(R.id.lvComm);

        editText = (EditText) findViewById(R.id.typeCommField);
        id = getIntent().getIntExtra("idHist", 123);
        idstr = String.valueOf(id);


        toolbar = (Toolbar) findViewById(R.id.commToolbar);
        //Активирует тулбар,т.е. делает тулбар экшн баром
        setSupportActionBar(toolbar);
        //Показывает кнопку "назад"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commentList = new ArrayList<HashMap<String, String>>();
        sharedUserID = getSharedPreferences("yourID", this.MODE_PRIVATE);
        userID = sharedUserID.getString("user_id", null);
        if (jParser.isOnline(this)) {
            new GetHistoryComments().execute();
        }
        Button buttComm = (Button) findViewById(R.id.sendComm);
        buttComm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (jParser.isOnline(Comments.this)) {
                    new CreateNewComment().execute();
                    editText.setText("");
                    recreate();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Не нашел как именно кнопки "назад" отследить нажатие, так что новых кнопок в меню пока не добавить
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

	class GetHistoryComments extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			params.add(new BasicNameValuePair("idstr", idstr));
			JSONObject json = jParser.makeHttpRequest(getString(R.string.url_get_comments), "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All Comments ", json.toString());

			try {
				// Checking for SUCCESS TAG
				
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					comments = json.getJSONArray("comments");
					Log.d("Success", " Yeap");
					//if (id == 10){
					// looping through All Products
					for (int i = 0; i < comments.length(); i++) {
						JSONObject c = comments.getJSONObject(i);

						String dateC = c.getString("login");
						String commC = c.getString(TAG_COMMENT);
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_DATE, dateC);
						map.put(TAG_COMMENT, commC);
						

						
						// adding HashList to ArrayList
						commentList.add(map);
						
					//}
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
			
			String[] from = {TAG_DATE,TAG_COMMENT};
			int[] to = {R.id.dateComm,R.id.commentSelf};
				ListAdapter adapter = new SimpleAdapter(getApplicationContext(), commentList, R.layout.comment_item, from,to) ;
				

				listView.setAdapter(adapter);
		}
		
	}
	
	class CreateNewComment extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}
		@Override
	    protected void onProgressUpdate(String... values) {
	       Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
	    }

		protected String doInBackground(String... args) {
			String etxt = editText.getText().toString();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("comment", etxt));
			params.add(new BasicNameValuePair("keyToHist", idstr));
            params.add(new BasicNameValuePair("userid",userID));
			JSONObject json = mjsp.makeHttpRequest(getString(R.string.url_add_comment),"POST", params);
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
			
			
		}
		
		
	}
	
	
	
	
}
