package de.hawlandshut.sharedwallet.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonSignIn;
    private Button mButtonCreateAccount;
    private Button mButtonForgotPassword;
    private TextView mTextViewError;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private LoadingDialog mLoadingDialog;
    private AuthViewModel mAuthViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mButtonSignIn = findViewById(R.id.btn_sign_in);
        mButtonCreateAccount = findViewById(R.id.btn_create_account);
        mButtonForgotPassword = findViewById(R.id.btn_forgot_password);
        mTextViewError = findViewById(R.id.tv_sign_in_error);
        mEditTextEmail = findViewById(R.id.et_sign_in_email);
        mEditTextPassword = findViewById(R.id.et_sign_in_password);

        mLoadingDialog = new LoadingDialog(this);
        mButtonSignIn.setOnClickListener(this);
        mButtonCreateAccount.setOnClickListener(this);
        mButtonForgotPassword.setOnClickListener(this);

        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthViewModel.getCurrentFirebaseUser().observe(this, currentUser ->{
            if(currentUser != null){
                finish();
            }
        });
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (i) {
            case R.id.btn_create_account:
                    gotoCreateAccount();
                return;

            case R.id.btn_sign_in:
                    signIn();
                return;

            case R.id.btn_forgot_password:
                    forgotPassword();
                return;
        }
    }

    private void signIn(){
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        mLoadingDialog.showDialog();

        mAuthViewModel.signIn(email,
                password).observe(this, signInResource -> {
                    mLoadingDialog.closeDialog();
            switch(signInResource.status){
                case SUCCESS:
                    Toast.makeText(getApplicationContext(), "You are signed in.", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                case ERROR:
                    mTextViewError.setText(signInResource.message);
                    return;
            }
        });
    }

    private void forgotPassword() {

       //TODO Implement Forgot Password Logic
    }

    private void gotoCreateAccount() {
        Intent intent = new Intent(getApplication(), CreateAccountActivity.class);
        startActivity(intent);
    }
}