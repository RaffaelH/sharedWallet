package de.hawlandshut.sharedwallet.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import de.hawlandshut.sharedwallet.model.entities.UserDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.model.methods.IAuthMethods;

public class AuthRepository implements IAuthMethods {
    private static AuthRepository instance;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String USER_COLLECTION_NAME = "users";
    private CollectionReference userCollection = db.collection(USER_COLLECTION_NAME);
    private int retryCount = 0;


    public static AuthRepository getInstance() {
        if(instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    @Override
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

    @Override
    public MutableLiveData<FirebaseUser> getCurrentFirebaseUser(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        MutableLiveData<FirebaseUser> firebaseUserLiveData= new MutableLiveData<>();
        firebaseUserLiveData.setValue(firebaseUser);
        return firebaseUserLiveData;
    }

    @Override
    public MutableLiveData<Resource<String>> createAccount(String email, String password, String displayName){
        Log.d("Auth","createAccount - Repository");
        MutableLiveData<Resource<String>> createAccountMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener(authTask -> {
                //set display name
                setUser(authTask.getUser().getUid(),email,displayName);
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

    @Override
    public void signOut(){
        firebaseAuth.signOut();
    }

    private void setUser(String uid,String email, String displayName){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token->{
                    UserDto user = new UserDto(
                            uid,
                            email,
                            displayName,
                            token,
                            new ArrayList<>()
                    );
                    userCollection.document(uid).set(user).addOnSuccessListener(success ->{
                        Log.d("SetUser", "success");
                    }).addOnFailureListener(failure -> {
                        Log.d("SetUser", failure.getMessage());
                    });
                }).addOnFailureListener(failure ->{
                    Log.d("GetToken", failure.getMessage());
                });
    }
}
