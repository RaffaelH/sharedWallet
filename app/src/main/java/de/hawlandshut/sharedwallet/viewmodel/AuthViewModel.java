package de.hawlandshut.sharedwallet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.google.firebase.auth.FirebaseUser;

import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.model.methods.IAuthMethods;
import de.hawlandshut.sharedwallet.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel implements IAuthMethods {
    private AuthRepository authRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    @Override
    public LiveData<Resource<String>> signIn(String email, String password) {
        return authRepository.signIn(email, password);
    }

    @Override
    public FirebaseUser getCurrentFirebaseUser (){
        return authRepository.getCurrentFirebaseUser();
    }

    @Override
    public LiveData<Resource<String>> createAccount(String email, String password, String displayName) {
        return authRepository.createAccount(email, password, displayName);
    }

    @Override
    public void signOut(){
        authRepository.signOut();
    }

}