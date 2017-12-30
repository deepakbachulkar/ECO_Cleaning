package com.ipws.eco.ui.activity;

import android.os.Bundle;

import com.ipws.eco.R;
import com.ipws.eco.ui.fragment.BaseFragment;
import com.ipws.eco.ui.fragment.LoginFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init()
    {
        BaseFragment fragment;
        String tag;
        fragment = new LoginFragment();
        tag = LoginFragment.TAG;
        clearBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
        checkPermissionsLocation();
    }
  

}

