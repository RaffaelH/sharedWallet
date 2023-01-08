package de.hawlandshut.sharedwallet.model.methods;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.FriendRequestDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;

public interface IInviteMethods {

    Uri generateContentLink();

    LiveData<Resource<String>> inviteFriend(UserInfoDto friendsInfo);

    LiveData<Resource<List<FriendRequestDto>>> getALlFriendRequests();

    LiveData<Resource<String>> updateFriendRequest(FriendRequestDto friendRequestDto);

}
