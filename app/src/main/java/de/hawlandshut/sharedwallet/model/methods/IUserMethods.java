package de.hawlandshut.sharedwallet.model.methods;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.UserDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;

/**
 * Interface to define the methods that are used in the UserRepository and UserViewModel
 */
public interface IUserMethods {

    void updateUserToken(String uid, String token);

    LiveData<Resource<List<UserInfoDto>>> searchUser(String displayName);

    LiveData<Resource<UserDto>> getSignedInUser();

}
