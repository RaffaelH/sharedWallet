package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.viewmodel.UserViewModel;
import de.hawlandshut.sharedwallet.views.components.AddFriendToGroupAdapter;
import de.hawlandshut.sharedwallet.views.components.LoadingDialog;

public class AddFriendToGroupActivity extends AppCompatActivity {

    private List<UserInfoDto> friends = new ArrayList<>();
    private GroupDto mGroup;
    private UserViewModel mUserViewModel;
    private GroupViewModel mGroupViewModel;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_to_group);
        mLoadingDialog = new LoadingDialog(this);

        RecyclerView recyclerView = findViewById(R.id.rv_friends_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AddFriendToGroupAdapter adapter = new AddFriendToGroupAdapter(this);
        recyclerView.setAdapter(adapter);

        mGroup = getIntent().getParcelableExtra("groupDto");
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        mLoadingDialog.showDialog();
        mUserViewModel.getSignedInUser().observe(this,result ->{
            mLoadingDialog.closeDialog();
            switch(result.status){
                case SUCCESS:
                    adapter.setFriends(result.data.getFriends());
                    break;
                case ERROR:
                    break;
            }
        });
        adapter.setFriends(friends);
    }

    public void updateMembers(UserInfoDto newMember){
        mLoadingDialog.showDialog();
        if(mGroup.getMemberIds().contains(newMember.getUserId())){
            mLoadingDialog.closeDialog();
            Toast.makeText(getApplicationContext(), "Dieser Freund ist bereits in der Gruppe enthalten.", Toast.LENGTH_LONG).show();
        }else{
            mGroupViewModel.updateMembers(mGroup.getGroupId(), newMember).observe(this, result -> {
                mLoadingDialog.closeDialog();
                switch(result.status){
                    case SUCCESS:
                        finish();
                        return;
                    case ERROR:
                        Toast.makeText(getApplicationContext(),"Etwas ist schief gelaufen.", Toast.LENGTH_LONG).show();
                        return;
                }
            });
        }


    }


}
