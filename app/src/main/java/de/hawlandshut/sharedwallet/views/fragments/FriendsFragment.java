package de.hawlandshut.sharedwallet.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.viewmodel.InviteViewModel;
import de.hawlandshut.sharedwallet.views.activities.CreateAccountActivity;
import de.hawlandshut.sharedwallet.views.activities.InviteFriendsActivity;

public class FriendsFragment extends Fragment implements View.OnClickListener {

    private InviteViewModel mFriendsViewModel;
    private Button mBtnInviteFriends;

    public FriendsFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnInviteFriends = view.findViewById(R.id.flt_btn_invite_friends);
        mFriendsViewModel = new ViewModelProvider(requireActivity()).get(InviteViewModel.class);
        mBtnInviteFriends.setOnClickListener(this);
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

    private void onShareClicked(){
        Uri link = mFriendsViewModel.generateContentLink();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link.toString());

        startActivity(Intent.createChooser(intent, "Share Link"));

    }

}