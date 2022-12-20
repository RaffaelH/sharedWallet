package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.views.fragments.GroupsFragment;

public class HomeActivity extends AppCompatActivity {

    private GroupViewModel mGroupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
    }
    public void reset(){
        mGroupViewModel.removeListener("AllGroups");
        finish();
    }

}