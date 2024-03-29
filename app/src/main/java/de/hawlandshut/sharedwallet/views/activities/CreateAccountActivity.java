package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;
import de.hawlandshut.sharedwallet.views.components.LoadingDialog;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextPasswordRep;
    private EditText mEditTextDisplayName;
    private Button mButtonCreateAccount;
    private TextView mTextViewCreateAccount;
    private AuthViewModel mAuthViewModel;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mEditTextEmail = findViewById( R.id.et_create_account_email );
        mEditTextPassword = findViewById( R.id.et_create_account_password );
        mEditTextPasswordRep = findViewById( R.id.et_create_account_password_rep );
        mEditTextDisplayName = findViewById(R.id.et_create_account_display_name);
        mButtonCreateAccount = findViewById( R.id.btn_create_account );
        mTextViewCreateAccount = findViewById(R.id.tv_create_account_error);

        mButtonCreateAccount.setOnClickListener( this );
        mLoadingDialog = new LoadingDialog(this);

        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (i) {
            case R.id.btn_create_account:
                doCreateAccount();
                return;

        }
    }
    private void doCreateAccount() {
        Toast.makeText(getApplication(), "pressed createAccount", Toast.LENGTH_LONG).show();
        Log.d("Auth","doCreateCalled");
        mLoadingDialog.showDialog();
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String passwordRep = mEditTextPasswordRep.getText().toString();
        String displayName = mEditTextDisplayName.getText().toString();

        if (password.length()<2 || passwordRep.length() < 2 ){
            Toast.makeText(getApplicationContext(), "Password to short", Toast.LENGTH_LONG).show();
            return;
        }

        if (!passwordRep.equals(password)){
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        mAuthViewModel.createAccount(email,password, displayName).observe(this,createAccountResource -> {
            mLoadingDialog.closeDialog();
            switch(createAccountResource.status){
                case SUCCESS:
                    Toast.makeText(getApplicationContext(), "Account Created.", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                case ERROR:
                    mTextViewCreateAccount.setText(createAccountResource.message);
                    Toast.makeText(getApplicationContext(), createAccountResource.message, Toast.LENGTH_LONG).show();
                    return;
            }
        });
    }
}