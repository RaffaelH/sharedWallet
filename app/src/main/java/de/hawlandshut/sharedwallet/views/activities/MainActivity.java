package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;


public class MainActivity extends AppCompatActivity {

    private AuthViewModel mAuthViewModel;
    private GroupViewModel mGroupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthViewModel.getCurrentFirebaseUser().observe(this, currentFirebaseUser -> {
            if (currentFirebaseUser == null) {
                Intent intent = new Intent(getApplication(), SignInActivity.class);
                startActivity(intent);
            } else if(currentFirebaseUser != null) {
                NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
                NavigationUI.setupWithNavController(bottomNavigationView, navController);
            }
        });
    }

    public void restart(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

}


