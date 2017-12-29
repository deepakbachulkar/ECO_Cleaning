package com.ipws.eco.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ipws.eco.R;
import com.ipws.eco.ui.fragment.BaseFragment;
import com.ipws.eco.ui.fragment.ChangePwFragment;
import com.ipws.eco.ui.fragment.ScheduleFragment;

/**
 * Created by arun on 12/08/15.
 */
public class ScheduleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void init() {
        BaseFragment fragment;
        String tag;
             fragment = new ScheduleFragment();
            tag = ChangePwFragment.TAG;
        clearBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
        }
        return super.onOptionsItemSelected(item);
    }


}
