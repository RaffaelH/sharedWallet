package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.views.fragments.GroupsFragment;


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
        mAuthViewModel.getCurrentFirebaseUser().observe(this, currentFirebaseUser -> {
            if (currentFirebaseUser == null) {
                Intent intent = new Intent(getApplication(), SignInActivity.class);
                startActivity(intent);
            } else if(currentFirebaseUser != null) {
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }



}


