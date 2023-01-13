package de.hawlandshut.sharedwallet.model.methods;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

import de.hawlandshut.sharedwallet.model.retro.Resource;

/**
 * Interface to define the methods that are used in the AuthRepository and AuthViewModel
 */
public interface IAuthMethods {

   LiveData<Resource<String>> signIn(String email, String password);

   FirebaseUser getCurrentFirebaseUser ();

   LiveData<Resource<String>> createAccount(String email, String password, String displayName);

   LiveData<Resource<String>> forgotPasswordEmail(String email);

   LiveData<Resource<String>> deleteAccount(String password);

   void signOut();

}
