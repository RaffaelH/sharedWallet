package de.hawlandshut.sharedwallet.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.views.activities.CreateGroupActivity;
import de.hawlandshut.sharedwallet.views.components.GroupListAdapter;
import de.hawlandshut.sharedwallet.views.components.LoadingDialog;

public class GroupsFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "GroupsFragment";
    private ExtendedFloatingActionButton mFltBtnNewGroup;
    private GroupViewModel mGroupViewModel;
    private LoadingDialog mLoadingDialog;

    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFltBtnNewGroup = getView().findViewById(R.id.flt_btn_new_group);
        mFltBtnNewGroup.setOnClickListener(this);
        mLoadingDialog = new LoadingDialog(view.getContext());
        RecyclerView recyclerView = getView().findViewById(R.id.rv_group_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        GroupListAdapter adapter = new GroupListAdapter();
        recyclerView.setAdapter(adapter);

        mGroupViewModel = new ViewModelProvider(requireActivity()).get(GroupViewModel.class);
        mLoadingDialog.showDialog();
        mGroupViewModel.getAllGroups().observe(this.getActivity(), (Resource<List<GroupDto>> obs) ->{
            mLoadingDialog.closeDialog();
            switch(obs.status){
                case SUCCESS:
                    Log.d("GroupFragment", obs.data.toString());
                    adapter.setGroups(obs.data);
                    break;
                case ERROR:
                    Log.d(TAG,obs.message);
                    break;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.flt_btn_new_group:
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
                return;
        }
    }

}