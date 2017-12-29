package com.ipws.eco.ui.fragment;

import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.ipws.eco.R;


/**
 * Created by ziffi on 9/4/17.
 */

public class BaseFragment extends DialogFragment
{
    View containerView;


    public View addToBaseFragment(View view) {
        View baseView = View.inflate(getActivity(), R.layout.fragment_base, null);
        containerView =  baseView.findViewById(R.id.relBaseFragmentContainer);
        ((RelativeLayout) containerView).addView(view);
        return baseView;
    }

    public  void showProgressBar(){
        if(getView()!=null)
            getView().findViewById(R.id.relBaseFragmentProgressBar).setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        if(getView()!=null)
            getView().findViewById(R.id.relBaseFragmentProgressBar).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

//    public void showAlertSnackBar(String message) {
//        if (getActivity() != null) {
//            ((BaseActivity) getActivity()).showErrorSnackBar(message, -1, null);
//        }
//    }

    public void showFragmentCopyRight(){
        if(getView()!=null)
            getView().findViewById(R.id.txtBottom).setVisibility(View.VISIBLE);
    }

    public void  showFragmentProgressBar(){
        if(getActivity()!=null){
            getActivity().findViewById(R.id.relBaseFragmentProgressBar).setVisibility(View.VISIBLE);
        }
    }
    public void  hideFragmentProgressBar(){
        if(getActivity()!=null){
            getActivity().findViewById(R.id.relBaseFragmentProgressBar).setVisibility(View.GONE);
        }
    }

}
