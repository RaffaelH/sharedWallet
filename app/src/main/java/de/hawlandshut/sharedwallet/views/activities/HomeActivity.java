package de.hawlandshut.sharedwallet.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.FriendRequestDto;
import de.hawlandshut.sharedwallet.viewmodel.InviteViewModel;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;

public class HomeActivity extends AppCompatActivity {

    private GroupViewModel mGroupViewModel;
    private InviteViewModel mInviteViewModel;
    private TextView mTvCount;
    private ImageView mIvMailIcon;
    private ArrayList<FriendRequestDto> friendRequestDtoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        mInviteViewModel = new ViewModelProvider(this).get(InviteViewModel.class);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        final View notifications = menu.findItem(R.id.item_menu_message).getActionView();

        mInviteViewModel.getALlFriendRequests().observe(this, requests ->{
            switch (requests.status){
                case SUCCESS:
                    friendRequestDtoList =  new ArrayList<>(requests.data);
                    int count = friendRequestDtoList.size();
                    if(count == 0){
                        mTvCount.setVisibility(View.INVISIBLE);
                    }else{
                        mTvCount.setVisibility(View.VISIBLE);
                        mTvCount.setText(String.valueOf(friendRequestDtoList.size()));
                    }
                    break;
                case ERROR:
                    friendRequestDtoList = new ArrayList<>();
                    mTvCount.setVisibility(View.INVISIBLE);
                    mTvCount.setEnabled(false);
                    break;
            }
        });
        mIvMailIcon = notifications.findViewById(R.id.iv_mail);
        mTvCount =  notifications.findViewById(R.id.txt_count);

        mIvMailIcon.setOnClickListener(v ->{
            if(friendRequestDtoList.size()>0){
                doStartFriendRequestActivity();
            }
        });
        return true;
    }

    public void reset(){
        mGroupViewModel.resetAllGroups();
        mGroupViewModel.removeListener();
        finish();
    }

    private void doStartFriendRequestActivity(){
        Intent intent = new Intent(getApplication(), FriendRequestActivity.class);
        intent.putParcelableArrayListExtra("friendRequests", friendRequestDtoList);
        startActivity(intent);
    }
}