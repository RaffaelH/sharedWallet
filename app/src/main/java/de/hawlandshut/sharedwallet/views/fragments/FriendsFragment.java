package de.hawlandshut.sharedwallet.views.fragments;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;

import com.google.firebase.firestore.auth.User;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.UserDto;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.viewmodel.InviteViewModel;
import de.hawlandshut.sharedwallet.viewmodel.UserViewModel;
import de.hawlandshut.sharedwallet.views.activities.CreateAccountActivity;
import de.hawlandshut.sharedwallet.views.activities.InviteFriendsActivity;
import de.hawlandshut.sharedwallet.views.components.FriendAdapter;

public class FriendsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "FriendsFragment";
    private Button mBtnInviteFriends;
    private UserViewModel mUserViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnInviteFriends = view.findViewById(R.id.flt_btn_invite_friends);
        mBtnInviteFriends.setOnClickListener(this);
        mUserViewModel =  new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        RecyclerView recyclerView = getView().findViewById(R.id.rv_friend_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FriendAdapter adapter = new FriendAdapter();
        recyclerView.setAdapter(adapter);

        mUserViewModel.getSignedInUser().observe(getActivity(), result ->{
            switch (result.status){
                case SUCCESS:
                    adapter.setFriends(result.data.getFriends());
                    break;
                case ERROR:
                    Log.d(TAG,result.message);
                    break;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firends, container, false);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.flt_btn_invite_friends:
                Intent intent = new Intent(getActivity(), InviteFriendsActivity.class);
                startActivity(intent);
            return;
        }
    }
}