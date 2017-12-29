package com.ipws.eco.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipws.eco.R;
import com.ipws.eco.ui.fragment.DashBroadFragment;
import com.ipws.eco.ui.fragment.NavigationDrawerFragment;
import com.ipws.eco.ui.fragment.SuccessFragment;
import com.ipws.eco.ui.interfaces.OnCheckPermissions;
import com.ipws.eco.ui.widgets.ScrimInsetsFrameLayout;
import com.ipws.eco.utils.CheckRuntimePermission;
import com.ipws.eco.utils.PermissionsList;
import com.ipws.eco.utils.RunTimePermissionRequestCode;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private TextView txtTitle, txtIcon;
//    private ImageView imgTitle;
    private Typeface mTypefaceFontAwesome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mActionbarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionbarToolbar);
        init();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mActionbarToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

//        setNavDrawer();
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init(){
        permissionsList = new PermissionsList();
        runTimePermissionRequestCode = new RunTimePermissionRequestCode();
        checkRuntimePermission = new CheckRuntimePermission(null);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTypefaceFontAwesome = Typeface.createFromAsset(getApplication().getAssets(), "fontawesome-webfont.ttf" );
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtIcon = (TextView) findViewById(R.id.txtIcon);
        txtIcon.setTypeface(mTypefaceFontAwesome);
    }

    private ScrimInsetsFrameLayout mNavDrawerContainer;
    private Toolbar mActionbarToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public void setNavDrawer()
    {
        mNavDrawerContainer = (ScrimInsetsFrameLayout) findViewById(R.id.scrimInsetsFrameLayout);
        if (mNavDrawerContainer != null) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (mDrawerLayout == null) {
                return;
            }

            if (mActionbarToolbar != null) {
                mActionbarToolbar.setVisibility(View.VISIBLE);
                mActionbarToolbar.setNavigationIcon(R.drawable.ic_menu);
                mActionbarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                    }
                });
            }

            mNavDrawerContainer = (ScrimInsetsFrameLayout) findViewById(R.id.scrimInsetsFrameLayout);
            mNavDrawerContainer.setVisibility(View.VISIBLE);

            mNavigationDrawerFragment = new NavigationDrawerFragment();
            mNavigationDrawerFragment.setNavDrawerItemCLickListener(new NavigationDrawerFragment.NavDrawerItemClickListener() {
                @Override
                public void onNavDrawerItemClick() {

                }
            });
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.scrimInsetsFrameLayout, mNavigationDrawerFragment).commit();
        }
    }

    public void hideToolBar(){
            if(mActionbarToolbar!=null)
                mActionbarToolbar.setVisibility(View.GONE);
    }

    public void setTitle(String title){
        if(mActionbarToolbar!=null)
            mActionbarToolbar.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            android.app.Fragment frg= getFragmentManager().findFragmentByTag(DashBroadFragment.TAG);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) { }
        else if (id == R.id.nav_gallery) { }
        else if (id == R.id.nav_slideshow) { }
        else if (id == R.id.nav_manage) { }
        else if (id == R.id.nav_share) { }
        else if (id == R.id.nav_send) { }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void setTitleIcon(String text, String title){
        if(mActionbarToolbar!=null)
            mActionbarToolbar.setVisibility(View.VISIBLE);
        txtIcon.setText(text);
        txtTitle.setText(title);
        if(mDrawerLayout!=null)
            mDrawerLayout.closeDrawers();
    }

    public void openDrawers(){
        if(mDrawerLayout!=null)
            mDrawerLayout.openDrawer(Gravity.LEFT);
    }
    //------------------------ Permission Checked -------------------------------------

    private PermissionsList permissionsList;
    private RunTimePermissionRequestCode runTimePermissionRequestCode;
    private CheckRuntimePermission checkRuntimePermission;
    public OnCheckPermissions mOnCheckPermissions;

    public void checkPermissionsLocation()
    {
        String[] permissionForStorage = new String[1];
        permissionForStorage[0] = permissionsList.ACCESS_FINE_LOCATION;
        int requestCodeStorage = runTimePermissionRequestCode.STORAGE_REQUEST_CODE;
        String DonotShowStorageRequestAgain = runTimePermissionRequestCode.DONOT_SHOW_STORAGE_REQUEST;
        String InfoMessage = runTimePermissionRequestCode.infoMessageSTORAGE;

        int isStorageAllowed = checkRuntimePermission.IsPermissionAllowed(this, this, permissionForStorage, requestCodeStorage, DonotShowStorageRequestAgain, InfoMessage);
        if (isStorageAllowed == runTimePermissionRequestCode.PERMISSION_GRANTED_CODE) {
            if(mOnCheckPermissions!=null)
                mOnCheckPermissions.onChecked("granted");
        } else if (isStorageAllowed == runTimePermissionRequestCode.DONOT_SHOW_PERMISSION_DIALOG_CODE) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Permission Required!");
            alertBuilder.setMessage(runTimePermissionRequestCode.toastMessageSTORAGE);
            alertBuilder.setPositiveButton("APP SETTINGS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (this== null) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });

            alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        } else {
//                Toast.makeText(this, "Permission for Storage is Denied", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isPermissionsLocation()
    {
        String[] permissionForStorage = new String[1];
        permissionForStorage[0] = permissionsList.ACCESS_FINE_LOCATION;
        int requestCodeStorage = runTimePermissionRequestCode.STORAGE_REQUEST_CODE;
        String DonotShowStorageRequestAgain = runTimePermissionRequestCode.DONOT_SHOW_STORAGE_REQUEST;
        String InfoMessage = runTimePermissionRequestCode.infoMessageSTORAGE;

        int isStorageAllowed = checkRuntimePermission.IsPermissionAllowed(this, this, permissionForStorage, requestCodeStorage, DonotShowStorageRequestAgain, InfoMessage);
        if (isStorageAllowed == runTimePermissionRequestCode.PERMISSION_GRANTED_CODE) {
            return true;
        } else if (isStorageAllowed == runTimePermissionRequestCode.DONOT_SHOW_PERMISSION_DIALOG_CODE) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Permission Required!");
            alertBuilder.setMessage(runTimePermissionRequestCode.toastMessageSTORAGE);
            alertBuilder.setPositiveButton("APP SETTINGS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (this== null) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });

            alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
            return false;
        } else {
            return false;
        }

    }

    public void  onOpenFragment(Fragment fragment)
    {

        String tag = SuccessFragment.TAG;
//        clearBackStack();
//        .addToBackStack(tag)
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
    }


    public void  onOpenFragmentWithBack(Fragment fragment)
    {
        String tag = SuccessFragment.TAG;
//        clearBackStack();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).addToBackStack(tag).commit();
    }

    public Typeface getTypefaceFontAwesome(){
        return mTypefaceFontAwesome;
    }



}
