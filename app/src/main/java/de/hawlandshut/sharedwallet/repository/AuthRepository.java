package de.hawlandshut.sharedwallet.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hawlandshut.sharedwallet.model.entity.Resource;
import de.hawlandshut.sharedwallet.model.entity.User;

public class AuthRepository {
    private final String TAG = "AuthRepository: ";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Application application;

    public MutableLiveData<Resource<String>> signIn(String email, String password) {
        MutableLiveData<Resource<String>> authenticationMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(authTask ->{
            if(authTask.isSuccessful()) {
                authenticationMutableLiveData.setValue(Resource.success("Success"));
            }
           else {
               authenticationMutableLiveData.setValue(Resource.error(authTask.getException().getMessage(),null));
           }
        });
        return authenticationMutableLiveData;
    }
    public MutableLiveData<FirebaseUser> getCurrentFirebaseUser(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        MutableLiveData<FirebaseUser> firebaseUserLiveData= new MutableLiveData<>();
        firebaseUserLiveData.setValue(firebaseUser);
        return firebaseUserLiveData;
    }

    public void signOut(){
        firebaseAuth.signOut();
    }

    public MutableLiveData<Resource<String>> createAccount(String email, String password){
        MutableLiveData<Resource<String>> createAccountMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(authTask -> {

           Log.d("AuthRepository, CreateAccount: ",authTask.getResult().toString());
            if(authTask.isSuccessful()){
                createAccountMutableLiveData.setValue(Resource.success("Success"));
            }
            else{
                createAccountMutableLiveData.setValue(Resource.error(authTask.getException().getMessage(),null));
            }
        });

        return createAccountMutableLiveData;
    }

}
