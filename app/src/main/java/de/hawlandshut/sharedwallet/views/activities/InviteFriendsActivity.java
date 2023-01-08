package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.utils.Validators;
import de.hawlandshut.sharedwallet.viewmodel.InviteViewModel;
import de.hawlandshut.sharedwallet.viewmodel.UserViewModel;
import de.hawlandshut.sharedwallet.views.components.LoadingDialog;
import de.hawlandshut.sharedwallet.views.components.SearchFriendListAdapter;

public class InviteFriendsActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEdtFriendName;
    private TextView mTvSearchFriendResult;
    private Button mBtnSearchFriens;
    private UserViewModel mUserViewModel;
    private InviteViewModel mInviteViewModel;
    private LoadingDialog mLoadingDialog;
    private SearchFriendListAdapter adapter = new SearchFriendListAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = new LoadingDialog(this);
        setContentView(R.layout.activity_invite_friends);
        mEdtFriendName =findViewById(R.id.et_invite_friend_name);
        mTvSearchFriendResult = findViewById(R.id.tv_search_friend_result);
        mTvSearchFriendResult.setSingleLine(false);
        mBtnSearchFriens = findViewById(R.id.btn_search_friend);
        mBtnSearchFriens.setOnClickListener(this);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mInviteViewModel = new ViewModelProvider(this).get(InviteViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.rv_search_friends_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
}

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_search_friend:
                searchUser();
        }
    }

    private void searchUser(){

        String displayName = mEdtFriendName.getText().toString();
        if(Validators.isNullOrEmpty(displayName)){
            mTvSearchFriendResult.setText("Name erforderlich");
            mEdtFriendName.setHintTextColor(Color.RED);
            return;
        }
        else{
            mLoadingDialog.showDialog();
            mTvSearchFriendResult.setText("");
            mEdtFriendName.setHintTextColor(Color.GRAY);
            mUserViewModel.searchUser(displayName).observe(this,result ->{
                mLoadingDialog.closeDialog();
                switch(result.status){
                    case SUCCESS:
                        adapter.setUsers(result.data);
                        if(result.data.isEmpty()){
                            mTvSearchFriendResult.setText("Keine Benutzer gefunden.\n Jetzt Freunde per Link zur App einladen!");

                        }
                        return;
                    case ERROR:
                        Log.d("Invite","failed");
                        return;
                }
            });
        }
    }

    public void inviteFriends(UserInfoDto friendsInfo){
        mLoadingDialog.showDialog();
        mInviteViewModel.inviteFriend(friendsInfo).observe(this, invite -> {
            mLoadingDialog.closeDialog();
            switch(invite.status){
                case SUCCESS:
                    finish();
                    break;
                case ERROR:
                    mTvSearchFriendResult.setText(invite.message);
                    break;
            }
        });

    }

}