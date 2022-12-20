package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.repository.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.views.components.LoadingDialog;

public class GroupEditActivity extends AppCompatActivity {

    private GroupViewModel mGroupViewModel;
    private LoadingDialog mLoadingDialog;
    private TextView mTvTitle;
    private TextView mTvMembers;
    private TextView mTvDescription;
    private TextView mTvErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);
        mLoadingDialog = new LoadingDialog(this);
        mTvTitle = findViewById(R.id.tv_group_edit_title);
        mTvMembers = findViewById(R.id.tv_group_edit_members);

        mLoadingDialog.showDialog();
        mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
           mGroupViewModel.getGroupById(extras.getString("groupId")).observe(this, groupDtoResource -> {
               mLoadingDialog.closeDialog();
               switch(groupDtoResource.status){
                   case SUCCESS:

                       Log.d("GroupEdit",groupDtoResource.data.getTitle()+" "+ groupDtoResource.data.getGroupId());
                       mTvTitle.setText(groupDtoResource.data.getTitle());
                       mTvMembers.setText(String.valueOf(groupDtoResource.data.getMemberNames()));
                       break;
                   case ERROR:
                       setContentView(R.layout.error_view);
                       mTvErrorMsg.setText(groupDtoResource.message);
                       break;
                   default:
                       Log.d("GroupEdit","default");
                       break;
               }
           });
        } else {
            setContentView(R.layout.error_view);
            mTvErrorMsg.setText("Keine Daten vorhanden.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGroupViewModel.removeListener();
    }
}