package de.hawlandshut.sharedwallet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.UserDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.model.methods.IUserMethods;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.repository.UserRepository;

/**
 * State Holder to the current users information.
 */
public class UserViewModel extends AndroidViewModel implements IUserMethods {

    private UserRepository userRepository;
    private LiveData<Resource<UserDto>>  currentUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
        currentUser = userRepository.getSignedInUser();
    }

    @Override
    public void updateUserToken(String uid, String token) {
        userRepository.updateUserToken(uid,token);
    }

    @Override
    public LiveData<Resource<List<UserInfoDto>>> searchUser(String displayName) {
        return userRepository.searchUser(displayName);
    }

    @Override
    public LiveData<Resource<UserDto>> getSignedInUser() {
        return currentUser;
    }
}
