package de.hawlandshut.sharedwallet.views.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;


public class MainActivity extends AppCompatActivity {

    private AuthViewModel mAuthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Main","onCreat called");
        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
       FirebaseUser currentFirebaseUser =  mAuthViewModel.getCurrentFirebaseUser();
            if (currentFirebaseUser == null) {
                Intent intent = new Intent(getApplication(), SignInActivity.class);
                startActivity(intent);
            } else if(currentFirebaseUser != null) {
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
            }

    }



}


