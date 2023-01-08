package de.hawlandshut.sharedwallet.repository;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.FriendRequestDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.model.methods.IInviteMethods;
import de.hawlandshut.sharedwallet.model.retro.Resource;

public class InviteRepository implements IInviteMethods {

    private static InviteRepository instance;
    private final Uri BASE_URL = Uri.parse("https://sharedwallet.page.link/app-install");
    private final String DOMAIN = "https://sharedwallet.page.link";
    private final String INVITES_COLLECTION_NAME = "invites";
    private final String USER_ID_FIELD="invitedId";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference invitesCollection = db.collection(INVITES_COLLECTION_NAME);

    public static InviteRepository getInstance(){
        if(instance == null){
            instance = new InviteRepository();
        }
        return instance;
    }

    @Override
    public Uri generateContentLink() {
        DynamicLink link = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(BASE_URL)
                .setDomainUriPrefix(DOMAIN)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("de.hawlandshut.sharedwallet").build())
                .buildDynamicLink();

        return link.getUri();
    }

    @Override
    public LiveData<Resource<String>> inviteFriend(UserInfoDto friendInfo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        invitesCollection.add(setFriendRequestDto(friendInfo)).addOnSuccessListener(success -> {
            liveData.setValue(Resource.success("success"));
        }).addOnFailureListener(failure -> {
            liveData.setValue(Resource.success(failure.getMessage()));
        });
        return liveData;
    }

    @Override
    public LiveData<Resource<List<FriendRequestDto>>> getALlFriendRequests() {
        MutableLiveData<Resource<List<FriendRequestDto>>> liveData = new MutableLiveData<>();
        invitesCollection.whereEqualTo(USER_ID_FIELD,FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            if(error != null){
                liveData.setValue(Resource.error(error.getMessage(), null));
            }
            if(value != null && !value.isEmpty()){
                List<DocumentSnapshot> documents = value.getDocuments();
                List<FriendRequestDto> friendRequestDtoList = toFriendRequestDtoList(documents);
                friendRequestDtoList = filterProcessedRequests(friendRequestDtoList);
                liveData.setValue(Resource.success(friendRequestDtoList));
            }
            else{
                liveData.setValue(Resource.error("keine Dokumente", null));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<Resource<String>> updateFriendRequest(FriendRequestDto friendRequestDto) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        DocumentReference dbRef = invitesCollection.document(friendRequestDto.getRequestId());
        dbRef.update("declined",friendRequestDto.getDeclined(),"processed",true)
                .addOnSuccessListener(success ->{
            liveData.setValue(Resource.success("success"));
        }).addOnFailureListener(failure ->{
            liveData.setValue(Resource.error(failure.getMessage(),null));
        });
        return liveData;
    }

    private FriendRequestDto setFriendRequestDto(UserInfoDto friendsInfo){
       String currentUserId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
       String currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
       FriendRequestDto friendRequestDto = new FriendRequestDto(
               "",
               currentUserId,
               currentUserName,
               friendsInfo.getUserId(),
               friendsInfo.getDisplayName(),
               false,
               false,
               new Date().getTime()
        );
        return friendRequestDto;
    }

    private List<FriendRequestDto> toFriendRequestDtoList(  List<DocumentSnapshot> documents){
        List<FriendRequestDto> requests = new ArrayList<>();

        for(int i = 0; i < documents.size(); i++){
            Boolean processed = (Boolean)documents.get(i).getData().get("processed");
            Boolean declined = (Boolean)documents.get(i).getData().get("declined");

            FriendRequestDto request = new FriendRequestDto(
                    (String) documents.get(i).getData().get("requestId"),
                    (String) documents.get(i).getData().get("inviterId"),
                    (String) documents.get(i).getData().get("inviterName"),
                    (String) documents.get(i).getData().get("invitedId"),
                    (String) documents.get(i).getData().get("invitedName"),
                    processed,
                    declined,
                    (Long) documents.get(i).getData().get("created")
            );
            requests.add(request);
        }
        return requests;
    }

    private  List<FriendRequestDto> filterProcessedRequests( List<FriendRequestDto> requests){

        List<FriendRequestDto>  filteredRequests;
        for(int i=0; i<requests.size();i++){
            if(requests.get(i).getProcessed()){
                requests.remove(requests.get(i));
            }
        }
        filteredRequests = requests;

        return filteredRequests;
    }
}
