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
import android.widget.Toast;


import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.utils.Validators;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvDisplayName = getView().findViewById(R.id.tv_account_displayName);
        mTvEmail = getView().findViewById(R.id.tv_account_email);
        mBtnSignOut = getView().findViewById(R.id.btn_account_sign_out);
        mBtnDelete = getView().findViewById(R.id.btn_account_delete_user);
        mEdtPassword = getView().findViewById(R.id.et_account_password);
        mBtnSignOut.setOnClickListener(this);
        mBtnDelete = getView().findViewById(R.id.btn_account_delete_user);
        mBtnDelete.setOnClickListener(this);
        mEdtPassword = getView().findViewById(R.id.et_account_password);
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mGroupViewModel = new ViewModelProvider(requireActivity()).get(GroupViewModel.class);

            mTvDisplayName.setText( mAuthViewModel.getCurrentFirebaseUser().getDisplayName());
            mTvEmail.setText( mAuthViewModel.getCurrentFirebaseUser().getEmail());

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
            case R.id.btn_account_delete_user:
                deleteAccount();
                return;
        }
    }

    private void deleteAccount(){
        String password = mEdtPassword.getText().toString();
        if(Validators.isNullOrEmpty(password)){
            Toast.makeText(getActivity(), "Es muss ein Passwort eingegeben werden.", Toast.LENGTH_LONG).show();
            mEdtPassword.setHighlightColor(getActivity().getColor(R.color.red));
            return;
        }
        else{
            mAuthViewModel.deleteAccount(password).observe(getActivity(),result ->{
                switch (result.status) {
                    case SUCCESS:
                        Toast.makeText(getActivity(), "Account erfolgreich gelöscht.", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                        break;
                    case ERROR:
                        Toast.makeText(getActivity(), result.message, Toast.LENGTH_LONG).show();
                        break;
                }
            });
        }

    }
}