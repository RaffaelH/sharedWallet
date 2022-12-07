package de.hawlandshut.sharedwallet.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import de.hawlandshut.sharedwallet.model.entity.Resource;


public class AuthRepository {
    private final String TAG = "AuthRepository: ";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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

    public MutableLiveData<Resource<String>> createAccount(String email, String password){
        Log.d("Auth","createAccount - Repository");
        MutableLiveData<Resource<String>> createAccountMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(authTask -> {


            if(authTask.isSuccessful()){
                createAccountMutableLiveData.setValue(Resource.success("Success"));
            }
        }).addOnFailureListener(authFailure ->{
            Log.d("Auth",authFailure.getMessage());
            createAccountMutableLiveData.setValue(Resource.error(authFailure.getMessage(),null));
        }).addOnCanceledListener(() -> {

        });

        return createAccountMutableLiveData;
    }

    public void signOut(){
        firebaseAuth.signOut();
    }

}
