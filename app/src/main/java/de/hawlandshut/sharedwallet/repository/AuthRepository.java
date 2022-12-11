package de.hawlandshut.sharedwallet.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import de.hawlandshut.sharedwallet.model.Resource;

public class AuthRepository {
    private final String TAG = "AuthRepository";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public MutableLiveData<Resource<String>> signIn(String email, String password) {
        MutableLiveData<Resource<String>> authenticationMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(authTask ->{
            if(authTask.isSuccessful()) {
                authenticationMutableLiveData.setValue(Resource.success("success"));
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

    public MutableLiveData<Resource<String>> createAccount(String email, String password, String displayName){
        Log.d("Auth","createAccount - Repository");
        MutableLiveData<Resource<String>> createAccountMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener(authTask -> {
                //set display name
                firebaseAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName).build()).addOnSuccessListener(update -> {
                    createAccountMutableLiveData.setValue(Resource.success("success"));
                }).addOnFailureListener(failure ->{
                    createAccountMutableLiveData.setValue(Resource.error(failure.getMessage(),null));
                });
            }).addOnFailureListener(authFailure ->{
            Log.d("Auth",authFailure.getMessage());
            createAccountMutableLiveData.setValue(Resource.error(authFailure.getMessage(),null));
        });
        return createAccountMutableLiveData;
    }

    public void signOut(){
        firebaseAuth.signOut();
    }

}
