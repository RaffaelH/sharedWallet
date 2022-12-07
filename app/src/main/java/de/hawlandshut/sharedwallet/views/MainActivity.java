package de.hawlandshut.sharedwallet.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AuthViewModel mAuthViewModel;
    private Button mBtnSignOut;
    private TextView mTextViewDisplayName;
    private TextView mTextViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnSignOut = findViewById(R.id.btn_sign_out_test);
        mTextViewEmail = findViewById(R.id.tv_main_email);
        mTextViewDisplayName = findViewById(R.id.tv_main_display_name);
        mBtnSignOut.setOnClickListener(this);
        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthViewModel.getCurrentFirebaseUser().observe(this, currentFirebaseUser -> {
            if (currentFirebaseUser == null) {
                Intent intent = new Intent(getApplication(), SignInActivity.class);
                startActivity(intent);
            } else {
                mTextViewDisplayName.setText("Display Name: "+ currentFirebaseUser.getDisplayName());
                mTextViewEmail.setText("Email: "+ currentFirebaseUser.getEmail());
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (i) {
            case R.id.btn_sign_out_test:
                  mAuthViewModel.signOut();
                Intent intent = new Intent(getApplication(), SignInActivity.class);
                startActivity(intent);
                return;
        }
    }
}