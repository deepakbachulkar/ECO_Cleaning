package com.ipws.eco.ui.activity;

import android.os.Bundle;

import com.ipws.eco.R;
import com.ipws.eco.ui.fragment.BaseFragment;
import com.ipws.eco.ui.fragment.ChangePwFragment;
import com.ipws.eco.ui.fragment.ForgotPwFragment;
import com.ipws.eco.ui.fragment.LoginFragment;

/**
 * A login screen that offers login via email/password.
 */
public class ForgotPwActivity extends BaseActivity
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
        fragment = new ForgotPwFragment();
        tag = ForgotPwFragment.TAG;
        clearBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
    }
}

