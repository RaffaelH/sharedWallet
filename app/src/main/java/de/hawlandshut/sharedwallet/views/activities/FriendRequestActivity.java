package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.FriendRequestDto;
import de.hawlandshut.sharedwallet.viewmodel.InviteViewModel;
import de.hawlandshut.sharedwallet.views.components.FriendRequestAdapter;
import de.hawlandshut.sharedwallet.views.components.LoadingDialog;
import de.hawlandshut.sharedwallet.views.components.SearchFriendListAdapter;

public class FriendRequestActivity extends AppCompatActivity {

    private ArrayList<FriendRequestDto> friendRequestList = new ArrayList<>();
    private LoadingDialog mLoadingDialog;
    private FriendRequestAdapter adapter = new FriendRequestAdapter(this);
    private InviteViewModel mInviteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        mLoadingDialog = new LoadingDialog(this);
        friendRequestList = new ArrayList<>(getIntent().getParcelableArrayListExtra("friendRequests"));
        mInviteViewModel = new ViewModelProvider(this).get(InviteViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.rv_friend_requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setRequests(friendRequestList);
    }

    public void updateFriendRequest(FriendRequestDto friendRequestDto){
        mLoadingDialog.showDialog();
        mInviteViewModel.updateFriendRequest(friendRequestDto).observe(this,updateResult->{
            mLoadingDialog.closeDialog();
            switch(updateResult.status){
                case SUCCESS:
                    finish();
                    return;
                case ERROR:
                    Toast.makeText(getApplicationContext(), updateResult.message, Toast.LENGTH_LONG).show();
                    return;
            }
        });
    }


}