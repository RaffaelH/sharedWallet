package de.hawlandshut.sharedwallet.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private final String USER_ID_FIELD = "userId";
    private CollectionReference userCollection = db.collection(USER_COLLECTION_NAME);
    private CollectionReference userInfoCollection = db.collection(USER_INFO_COLLECTION_NAME);
    private MutableLiveData<Resource<UserDto>> currentUserLiveData = new MutableLiveData<>();
    private ListenerRegistration userListener;

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

    @Override
    public LiveData<Resource<UserDto>> getSignedInUser() {
        userListener = userCollection
                .whereEqualTo(USER_ID_FIELD,auth.getCurrentUser().getUid())
                .addSnapshotListener(((value, error) -> {
            if(error != null){
                currentUserLiveData.setValue(Resource.error(error.getMessage(), null));
            }
            else if(value!= null && !value.isEmpty()){
                currentUserLiveData.setValue(Resource.success(setUserDto(value.getDocuments())));
            }
        }));
        return currentUserLiveData;
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

    private UserDto setUserDto(List<DocumentSnapshot> documents){

       ArrayList<Map<String,String>> friendDocs = (ArrayList<Map<String,String>> ) documents.get(0).getData().get("friends");
       List<UserInfoDto> friendsList = new ArrayList<>();
        Log.d("friendDocs: ", friendDocs.toString());

        for(int i = 0; i< friendDocs.size();i++){
            String displayName ="";
            String userId = "";
            for(Map.Entry<String,String> entry: friendDocs.get(i).entrySet()){

                if(entry.getKey().equals("displayName")){
                    displayName = entry.getValue();
                }
                if(entry.getKey().equals("userId")){
                    userId = entry.getValue();
                }
            }
            UserInfoDto friend = new UserInfoDto(
                    displayName,
                    userId
            );
            friendsList.add(friend);
            Log.d("friendList: ", friendsList.toString());
        }

        UserDto user = new UserDto(
                (String) documents.get(0).getData().get("userId"),
                (String) documents.get(0).getData().get("email"),
                (String) documents.get(0).getData().get("displayName"),
                (String) documents.get(0).getData().get("token"),
                friendsList
        );
        return user;
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
