package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.views.components.LoadingDialog;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdtGroupTitle;
    private Button mBtnCreateGroup;
    private AuthViewModel mAuthViewModel;
    private GroupViewModel mGroupViewModel;
    private LoadingDialog mLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mEdtGroupTitle = findViewById(R.id.et_create_group_title);
        mBtnCreateGroup = findViewById(R.id.btn_create_group);
        mLoadingDialog = new LoadingDialog(this);
        mBtnCreateGroup.setOnClickListener(this);
        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_create_group:
                if(mEdtGroupTitle != null){
                    addGroup();
                }
        }
    }

    private void addGroup(){
        mLoadingDialog.showDialog();
        mGroupViewModel.addGroup(setCreateGroupDto()).observe(this,addGroup -> {
            mLoadingDialog.closeDialog();
            switch(addGroup.status){
                case SUCCESS:
                    Toast.makeText(getApplicationContext(), "You are signed in.", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                case ERROR:
                    Toast.makeText(getApplicationContext(), addGroup.message, Toast.LENGTH_LONG).show();
                    return;
            }
        });
    }

    private GroupDto setCreateGroupDto(){
        GroupDto groupDto = new GroupDto(
                "",
                mEdtGroupTitle.getText().toString(),
                Arrays.asList(new String[]{mAuthViewModel.getCurrentFirebaseUser().getValue().getDisplayName()}),
                Arrays.asList(new String[]{mAuthViewModel.getCurrentFirebaseUser().getValue().getUid()}),
                mAuthViewModel.getCurrentFirebaseUser().getValue().getUid(),
                new Date().getTime()
                );
        return groupDto;
    }

}