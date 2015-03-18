package com.dustjfree.sap;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by free on 02.03.2015.
 */
public class History_expand extends ActionBarActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_expand);
        String hist = getIntent().getStringExtra("expandHistory");
        TextView tv = (TextView)findViewById(R.id.historyTextExp);
        tv.setText(hist);
        tv.setMovementMethod(new ScrollingMovementMethod());
        toolbar = (Toolbar)findViewById(R.id.historytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        finish();

        return super.onOptionsItemSelected(item);
    }
}
