package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;

public class GroupEditActivity extends AppCompatActivity {

    private GroupViewModel mGroupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);

        mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
           mGroupViewModel.getGroupById(extras.getString("groupId")).observe(this, groupDtoResource -> {
               switch(groupDtoResource.status){
                   case SUCCESS:
                       Log.d("GroupEdit",groupDtoResource.data.getTitle()+" "+ groupDtoResource.data.getGroupId());
                   case ERROR:
                     //  Log.d("GroupEdit",groupDtoResource.message);
               }
           });
        } else {
            Log.e("GroupEditError","Keine Resource erhalten");
        }
    }
}