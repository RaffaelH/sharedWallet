package de.hawlandshut.sharedwallet.model.methods;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

import de.hawlandshut.sharedwallet.model.entities.Resource;

public interface IAuthMethods {

   LiveData<Resource<String>> signIn(String email, String password);

   LiveData<FirebaseUser> getCurrentFirebaseUser ();

   LiveData<Resource<String>> createAccount(String email, String password, String displayName);

   void signOut();

}
