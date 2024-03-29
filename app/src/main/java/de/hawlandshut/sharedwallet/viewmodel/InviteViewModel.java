package de.hawlandshut.sharedwallet.viewmodel;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.FriendRequestDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.model.methods.IInviteMethods;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.repository.InviteRepository;

/**
 * ViewModel for all Invitations. State Holder to all FriendRequests of a user.
 */
public class InviteViewModel extends AndroidViewModel implements IInviteMethods {

    private InviteRepository friendsRepository;

    public InviteViewModel(@NonNull Application application) {
        super(application);
        friendsRepository = InviteRepository.getInstance();
    }

    @Override
    public Uri generateContentLink() {
        return friendsRepository.generateContentLink();

    }

    @Override
    public LiveData<Resource<String>> inviteFriend(UserInfoDto friendsInfo) {
        return friendsRepository.inviteFriend(friendsInfo);
    }

    @Override
    public LiveData<Resource<List<FriendRequestDto>>> getALlFriendRequests() {
        return friendsRepository.getALlFriendRequests();
    }

    @Override
    public LiveData<Resource<String>> updateFriendRequest(FriendRequestDto friendRequestDto) {
        return friendsRepository.updateFriendRequest(friendRequestDto);
    }
}
