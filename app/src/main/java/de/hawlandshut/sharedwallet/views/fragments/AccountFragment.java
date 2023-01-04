package de.hawlandshut.sharedwallet.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.views.activities.HomeActivity;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AccountFragment";
    private TextView mTvDisplayName;
    private TextView mTvEmail;
    private Button mBtnSignOut;
    private Button mBtnDelete;
    private EditText mEdtPassword;
    private AuthViewModel mAuthViewModel;
    private GroupViewModel mGroupViewModel;

    public AccountFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvDisplayName = getView().findViewById(R.id.tv_account_displayName);
        mTvEmail = getView().findViewById(R.id.tv_account_email);
        mBtnSignOut = getView().findViewById(R.id.btn_account_sign_out);
        mBtnDelete = getView().findViewById(R.id.btn_account_delete_user);
        mEdtPassword = getView().findViewById(R.id.et_account_password);
        mBtnSignOut.setOnClickListener(this);
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mGroupViewModel = new ViewModelProvider(requireActivity()).get(GroupViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_account_sign_out:
                mAuthViewModel.signOut();
                ((HomeActivity)getActivity()).reset();
                return;
        }


    }


}