package de.hawlandshut.sharedwallet.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextPasswordRep;
    private Button mButtonCreateAccount;
    private TextView mTextViewCreateAccount;
    private AuthViewModel mAuthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mEditTextEmail = findViewById( R.id.et_create_account_email );
        mEditTextPassword = findViewById( R.id.et_create_account_password );
        mEditTextPasswordRep = findViewById( R.id.et_create_account_password_rep );
        mButtonCreateAccount = findViewById( R.id.btn_create_account );
        mTextViewCreateAccount = findViewById(R.id.tv_create_account_error);

        mButtonCreateAccount.setOnClickListener( this );

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

        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String password1 = mEditTextPasswordRep.getText().toString();

        if (password.length()<2 || password1.length() < 2 ){
            Toast.makeText(getApplicationContext(), "Password to short", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password1.equals(password)){
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        mAuthViewModel.createAccount(email,password).observe(this,createAccountResource -> {
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