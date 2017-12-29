package com.ipws.eco.ui.activity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ipws.eco.R;
import com.ipws.eco.ui.fragment.BaseFragment;
import com.ipws.eco.ui.fragment.ChangePwFragment;
import com.ipws.eco.ui.fragment.ClockInOutFragment;
import com.ipws.eco.ui.fragment.DashBroadFragment;

/**
 * Created by arun on 12/08/15.
 */
public class HomeActivity extends BaseActivity // implements LocationListener
{

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
             fragment = new DashBroadFragment();
            tag = DashBroadFragment.TAG;
        clearBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).addToBackStack(tag).commit();
//        getLocation();
        checkPermissionsLocation();
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    LocationManager locationManager;
//    void getLocation() {
//        try {
//            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void onLocationChanged(Location location) {
//        ECOAppControl.setLocation(location);
////        Toast.makeText(this, "Current Loc: "+location.getLatitude()+" || "+location.getLongitude(), Toast.LENGTH_LONG).show();
////        locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//    }
}
