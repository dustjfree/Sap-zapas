package com.dustjfree.sap;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by free on 02.03.2015.
 */
public class Historie_expand extends Histories {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historie_expand);
        toolbar = (Toolbar)findViewById(R.id.historietoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        finish();

        return super.onOptionsItemSelected(item);
    }
}
