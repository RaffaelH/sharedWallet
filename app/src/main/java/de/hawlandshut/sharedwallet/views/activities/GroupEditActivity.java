package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
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
        mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);

            GroupDto group = getIntent().getParcelableExtra("groupDto");
            mTvTitle.setText(group.getTitle());
            mTvMembers.setText(String.valueOf(group.getMemberNames()));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGroupViewModel.removeListener();
    }
}