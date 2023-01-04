package de.hawlandshut.sharedwallet.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.UserDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.model.methods.IUserMethods;
import de.hawlandshut.sharedwallet.model.retro.Resource;

public class UserRepository implements IUserMethods {

    private static UserRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final String USER_COLLECTION_NAME = "users";
    private final String USER_INFO_COLLECTION_NAME = "userinfo";
    private final String DISPLAY_NAME_FIELD = "displayName";
    private CollectionReference userCollection = db.collection(USER_COLLECTION_NAME);
    private CollectionReference userInfoCollection = db.collection(USER_INFO_COLLECTION_NAME);

    public static UserRepository getInstance(){
        if(instance == null){
            instance = new UserRepository();
        }
        return instance;
    }

    @Override
    public void updateUserToken(String uid,String token) {
                userCollection.document(uid).update("token",token).addOnSuccessListener(success ->{
                    Log.d("UpdateToken", "Success");
                }).addOnFailureListener(failure -> {
                    Log.d("UpdateToken", "Failure");
                });
    }

    @Override
    public LiveData<Resource<List<UserInfoDto>>> searchUser(String displayName) {
        MutableLiveData<Resource<List<UserInfoDto>>> liveData = new MutableLiveData<>();
        userInfoCollection.whereGreaterThanOrEqualTo(DISPLAY_NAME_FIELD,displayName.toUpperCase())
                .whereLessThanOrEqualTo(DISPLAY_NAME_FIELD,displayName.toLowerCase()+'\uf8ff')
                .limit(10).get()
                .addOnSuccessListener(success ->{
            Log.d("searchUser",success.getDocuments().toString());
            liveData.setValue(Resource.success(setUserInfoDto(success.getDocuments())));
        }).addOnFailureListener(failure ->{
            liveData.setValue(Resource.error(failure.getMessage(),null));
            Log.d("searchUser",failure.getMessage());
        });
        return liveData;
    }

    private List<UserInfoDto> setUserInfoDto(List<DocumentSnapshot> documents){
        List<UserInfoDto> userInfoList = new ArrayList<>();
        for(int i =0; i< documents.size(); i++){

            UserInfoDto userInfo = new UserInfoDto(
                    (String) documents.get(i).getData().get("displayName"),
                    (String) documents.get(i).getData().get("userId")
            );
            userInfoList.add(userInfo);
            Log.d("searchUser",userInfoList.get(i).getDisplayName());
        }
        if(!userInfoList.isEmpty()){
            userInfoList = filterCurrentUser(userInfoList);
        }
        return userInfoList;
    }

    private List<UserInfoDto> filterCurrentUser(List<UserInfoDto> users){
        List<UserInfoDto> filteredUsers = users;
        String userId = auth.getCurrentUser().getUid();
        for(int i = 0; i< users.size(); i++){
            if(users.get(i).getUserId().equals(userId)){
                filteredUsers.remove(users.get(i));
            }
        }
        return filteredUsers;
    }
}
