package de.hawlandshut.sharedwallet.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.google.firebase.auth.FirebaseUser;

import de.hawlandshut.sharedwallet.model.entity.Resource;
import de.hawlandshut.sharedwallet.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private LiveData<Resource<String>> authenticatedUserLiveData;
    private LiveData<Resource<String>> createAccountLiveData;
    private LiveData<FirebaseUser> currentFirebaseUser;

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public LiveData<Resource<String>> signIn(String email, String password) {
        authenticatedUserLiveData = authRepository.signIn(email, password);
        return authenticatedUserLiveData;
    }

    public LiveData<FirebaseUser> getCurrentFirebaseUser (){
        currentFirebaseUser = authRepository.getCurrentFirebaseUser();
        return currentFirebaseUser;
    }

    public LiveData<Resource<String>> createAccount(String email, String password) {
        Log.d("Auth","createAccount - ViewModel");
        createAccountLiveData = authRepository.createAccount(email, password);
        return createAccountLiveData;
    }

}