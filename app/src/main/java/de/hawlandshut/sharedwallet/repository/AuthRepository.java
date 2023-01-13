package de.hawlandshut.sharedwallet.repository;

import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import de.hawlandshut.sharedwallet.model.entities.UserDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.model.methods.IAuthMethods;

/**
 * The AuthRepository handles all requests which are related to the authentication of a User.
 * Implements IAuthMethods.
*/
public class AuthRepository implements IAuthMethods {
    private static AuthRepository instance;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String USER_COLLECTION_NAME = "users";
    private CollectionReference userCollection = db.collection(USER_COLLECTION_NAME);
    private int retryCount = 0;


    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    /**
     * Firebase login with email and password
     * @param email
     * @param password
     * @return success message or error from backend
     */
    @Override
    public MutableLiveData<Resource<String>> signIn(String email, String password) {
        MutableLiveData<Resource<String>> authenticationMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                authenticationMutableLiveData.setValue(Resource.success("success"));
            } else {
                authenticationMutableLiveData.setValue(Resource.error(authTask.getException().getMessage(), null));
            }
        });
        return authenticationMutableLiveData;
    }

    /**
     * @return the current authenticated User.
     */
    @Override
    public FirebaseUser getCurrentFirebaseUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        return firebaseUser;
    }

    /**
     * Creates a new account with email and password.
     * Updates the users display name.
     * @param email
     * @param password
     * @param displayName
     * @return success message or error from backend
     */
    @Override
    public MutableLiveData<Resource<String>> createAccount(String email, String password, String displayName) {

        MutableLiveData<Resource<String>> createAccountMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authTask -> {
                    //set display name
                    setUser(authTask.getUser().getUid(), email, displayName);
                    firebaseAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName).build()).addOnSuccessListener(update -> {
                        createAccountMutableLiveData.setValue(Resource.success("success"));
                    }).addOnFailureListener(failure -> {
                        createAccountMutableLiveData.setValue(Resource.error(failure.getMessage(), null));
                    });
                }).addOnFailureListener(authFailure -> {
                    createAccountMutableLiveData.setValue(Resource.error(authFailure.getMessage(), null));
                });

        return createAccountMutableLiveData;
    }

    /**
     * Sends a password reset email.
     * @param email
     * @return success message or error from backend
     */
    @Override
    public LiveData<Resource<String>> forgotPasswordEmail(String email) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(success -> {
            liveData.setValue(Resource.success("success"));
        }).addOnFailureListener(failure -> {
            liveData.setValue(Resource.error(failure.getMessage(), null));
        }).addOnCanceledListener(() -> {
            liveData.setValue(Resource.error("Request cancelled", null));
        });

        return liveData;
    }

    /**
     * Deletes the account of a user.
     * @param password
     * @return success message or error from backend
     */
    @Override
    public LiveData<Resource<String>> deleteAccount(String password) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        AuthCredential credential;
        String email = firebaseAuth.getCurrentUser().getEmail();
        credential = EmailAuthProvider.getCredential(email, password);
        firebaseAuth.getCurrentUser().reauthenticate(credential).addOnSuccessListener(success -> {
            firebaseAuth.getCurrentUser().delete().addOnSuccessListener(deletionSuccess -> {
                liveData.setValue(Resource.success("success"));
            }).addOnFailureListener(deletionFailure -> {
                liveData.setValue(Resource.error(deletionFailure.getMessage(), null));
            }).addOnCanceledListener(() -> {
                liveData.setValue(Resource.error("Deletion Canceled", null));
            });
        }).addOnFailureListener(failure -> {
            liveData.setValue(Resource.error(failure.getMessage(), null));
        }).addOnCanceledListener(() -> {
            liveData.setValue(Resource.error("Reauthentication Canceled", null));
        });

        return liveData;
    }

    /**
     * Sign Out User
     */
    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }

    /**
     * Set Users Information to Users Collection.
     * @param uid
     * @param email
     * @param displayName
     */
    private void setUser(String uid, String email, String displayName) {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            UserDto user = new UserDto(
                    uid,
                    email,
                    displayName,
                    token,
                    new ArrayList<>()
            );
            userCollection.document(uid).set(user).addOnSuccessListener(success -> {
                Log.d("SetUser", "Set User Success");
            });
        }).addOnFailureListener(failure -> {
            Log.d("SetUser", "Set User Failed");
        });
    }
}
