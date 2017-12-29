package com.ipws.eco.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ipws.eco.ECOAppControl;
import com.ipws.eco.R;
import com.ipws.eco.ui.activity.HomeActivity;
import com.ipws.eco.ui.activity.LoginActivity;
import com.ipws.eco.utils.AppPreference;

public class SplashFragment extends BaseFragment
{
    public static String TAG= "splash";
    private final Handler mHideHandler = new Handler();

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            toNext();
        }
    };

    private OnFragmentInteractionListener mListener;

    public SplashFragment() {
    }

    public static SplashFragment newInstance(Bundle bundle) {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_splash, container, false);
        init(view);
        if(!AppPreference.getInstance(getActivity()).getLogin().equals(""))
        {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        return addToBaseFragment(view);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void init(View v){
        v.findViewById(R.id.txtBtnSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHideHandler.removeCallbacks(mHidePart2Runnable);
                toNext();
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        delay();
        showFragmentCopyRight();
//        Intent intent = new Intent(getActivity(), HomeActivity.class);
//        startActivity(intent);
//        getActivity().finish();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else { }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void delay() {
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, 5000);
    }

    private void toNext(){
        if(getActivity()!=null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}