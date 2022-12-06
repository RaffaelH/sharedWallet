package de.hawlandshut.sharedwallet.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity {

    private AuthViewModel mAuthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

       mAuthViewModel.getCurrentFirebaseUser().observe(this,currentFirebaseUser ->{
           if(currentFirebaseUser  == null ){
               Intent intent = new Intent(getApplication(), SignInActivity.class);
               startActivity(intent);
           }
           else{
               //TODO: implement logic if User is Authenticated
           }


       });

    }
}