package com.dustjfree.sap;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.internal.transition.ActionBarTransition;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class Histories extends ActionBarActivity {
	JSONparser jParser = new JSONparser();
	ListView lv;
    ListView menuList;
	ArrayList<HashMap<String, String>> historyList;
	JSONArray histories = null;
    SwipeRefreshLayout swipeRefreshLayout;
    HistoriesAdapter historiesAdapter;
	private ActionBarDrawerToggle toggle;
    private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "histories";
	private static final String TAG_ID = "id";
	private static final String TAG_HIST = "history";
	private static final String TAG_PLUS = "likeC";
	private static final String TAG_MINUS = "dislikeC";
	private static final String TAG_COMM = "commentC";
	private static final String TAG_DATE = "addDate";
    FloatingActionButton fabButton;
    int mPreviousFirstVisibleItem;
    int mLastScrollY;
    int mScrollThreshold;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.histories);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color, R.color.Green, R.color.Yellow, R.color.Red);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

		historyList = new ArrayList<HashMap<String, String>>();
		lv = (ListView)findViewById(R.id.listHist);
		new LoadAllHistories().execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                historyList.clear();
                new LoadAllHistories().execute();
            }
        });


        String[] web = {
                "Анонимные истории",
                "Консультация психолога",
                "Консультация юриста"
        } ;
        Integer[] imageId = {
                R.drawable.histories_new,
                R.drawable.psychologist_new,
                R.drawable.lawyer_new
        };
        SettingAdapter sad = new SettingAdapter(Histories.this, web, imageId);
        ListView lv = (ListView) findViewById(R.id.lv_navigation_drawer);
        lv.setAdapter(sad);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                if(position == 0) {
                    Intent intent = new Intent(Histories.this, Histories.class);
                    startActivity(intent);
                    finish();
                }
                if(position == 1) {
                    Intent intent = new Intent(Histories.this, PsihAct.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(Histories.this, YuristsAct.class);
                    startActivity(intent);
                }
            }
        });
/*
*COOOOMMMMMEEEEENT
 */
        fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.fab_send_hist_white__24dp))
                .withButtonColor(getResources().getColor(R.color.accent_color_400))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 8, 8)
                .create();
        FabListener fl = new FabListener() {
            @Override
            public void ButtonPressed() {
                Intent intent = new Intent(Histories.this,SendHist.class);
                startActivity(intent);
            }
        };
        fabButton.setListener(fl);
        this.lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount == 0) return;
                if(isSameRow(firstVisibleItem)){
                    int newScrollY = getTopItemScrollY();
                    boolean isSignificationDelta = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;
                    if(isSignificationDelta){
                        if(mLastScrollY>newScrollY){
                            fabButton.hideFloatingActionButton();
                        }else{
                            fabButton.showFloatingActionButton();
                        }
                    }
                    mLastScrollY = newScrollY;
                } else {
                    if(firstVisibleItem > mPreviousFirstVisibleItem){
                        fabButton.hideFloatingActionButton();
                    }else {
                        fabButton.showFloatingActionButton();
                    }
                    mLastScrollY = getTopItemScrollY();
                    mPreviousFirstVisibleItem = firstVisibleItem;
                }
            }
        });
	}
    boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == mPreviousFirstVisibleItem;
    }

    int getTopItemScrollY() {
        if (lv == null || lv.getChildAt(0) == null) return 0;
        View topChild = lv.getChildAt(0);
        return topChild.getTop();
    }
    void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

	public void Back_OnClick(View view){
		Intent intent = new Intent(Histories.this,Settings.class);
		startActivity(intent);
	}
	
	public void SendHistonClick(View view){
		Intent intent = new Intent(Histories.this, SendHist.class);
		startActivity(intent);
	}

    void onRefreshComplete(){
        //historiesAdapter.clear();

        swipeRefreshLayout.setRefreshing(false);
    }

	class LoadAllHistories extends AsyncTask<String, String, String>{
		private ProgressDialog pDialog;
	      @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            
	            /*pDialog = new ProgressDialog(Histories.this);
	            pDialog.setMessage("Загружаем ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	      */
	      }
		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(getString(R.string.url_get_histories), "GET", params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					histories = json.getJSONArray(TAG_PRODUCTS);
					Log.d("Success", " Yeap");
					
					for (int i = 0; i < histories.length(); i++) {
						JSONObject c = histories.getJSONObject(i);

						String id = c.getString(TAG_ID);
						String name = c.getString(TAG_HIST);
						String comCount = c.getString("comments");
					
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(TAG_ID, id);
						map.put(TAG_HIST, name);
						map.put("comments", comCount);
						historyList.add(map);

					}
				} else { }
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
		protected void onPostExecute(String file_url) {
            historiesAdapter = new HistoriesAdapter(Histories.this,historyList)
            {
                @Override
                public View getView(final int pos, View convertView, ViewGroup parent){
                    View row = super.getView(pos, convertView,parent);
                    View commB = row.findViewById(R.id.commentButton);
                    commB.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            JSONObject c;
                            try {
                                c = histories.getJSONObject(pos);
                                int id = c.getInt(TAG_ID);
                                Intent intent = new Intent(Histories.this, Comments.class);
                                intent.putExtra("idHist", id);
                                startActivity(intent);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    });

                    return row;
                }
            };
            lv.setAdapter(historiesAdapter);
            onRefreshComplete();

        }
	}
}
