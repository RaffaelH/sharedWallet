package de.hawlandshut.sharedwallet.repository;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    private final String INVITED_ID_FIELD = "invitedId";
    private final String INVITER_ID_FIELD = "inviterId";
    private final String PROCESSED_FIELD = "processed";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference invitesCollection = db.collection(INVITES_COLLECTION_NAME);

    public static InviteRepository getInstance() {
        if (instance == null) {
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
        Task<QuerySnapshot> friendQuery = invitesCollection.whereEqualTo(INVITED_ID_FIELD, friendInfo.getUserId()).whereEqualTo(INVITER_ID_FIELD, FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
        Task<QuerySnapshot> selfQuery = invitesCollection.whereEqualTo(INVITER_ID_FIELD, friendInfo.getUserId()).whereEqualTo(INVITED_ID_FIELD, FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
        friendQuery.addOnSuccessListener(querySuccess -> {
            if (querySuccess.isEmpty()) {
                selfQuery.addOnSuccessListener(selfQuerySuccess -> {
                    if (selfQuerySuccess.isEmpty()) {
                        invitesCollection.add(setFriendRequestDto(friendInfo)).addOnSuccessListener(success -> {
                            liveData.setValue(Resource.success("success"));
                        }).addOnFailureListener(failure -> {
                            liveData.setValue(Resource.error(failure.getMessage(), null));
                        }).addOnCanceledListener(() -> {
                            liveData.setValue(Resource.error("Invite canceled", null));
                        });
                    } else {
                        liveData.setValue(Resource.error("Es existiert bereits eine Einladung.", null));
                    }
                }).addOnFailureListener(selfQueryFailed -> {
                    liveData.setValue(Resource.error(selfQueryFailed.getMessage(), null));
                }).addOnCanceledListener(() -> {
                    liveData.setValue(Resource.error("Invite canceled", null));
                });
            } else {
                liveData.setValue(Resource.error("Es existiert bereits eine Einladung.", null));
            }

        }).addOnCanceledListener(() -> {
            liveData.setValue(Resource.error("Invite canceled", null));

        }).addOnFailureListener(queryFailed -> {
            liveData.setValue(Resource.error(queryFailed.getMessage(), null));
        });

        return liveData;
    }

    @Override
    public LiveData<Resource<List<FriendRequestDto>>> getALlFriendRequests() {
        MutableLiveData<Resource<List<FriendRequestDto>>> liveData = new MutableLiveData<>();
        invitesCollection.whereEqualTo(INVITED_ID_FIELD, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo(PROCESSED_FIELD, false).addSnapshotListener((value, error) -> {
                    if (error != null) {
                        liveData.setValue(Resource.error(error.getMessage(), null));
                    }
                    if (value != null && !value.isEmpty()) {
                        List<DocumentSnapshot> documents = value.getDocuments();
                        List<FriendRequestDto> friendRequestDtoList = toFriendRequestDtoList(documents);
                        liveData.setValue(Resource.success(friendRequestDtoList));
                    } else {
                        liveData.setValue(Resource.error("keine Dokumente", null));
                    }
                });
        return liveData;
    }

    @Override
    public LiveData<Resource<String>> updateFriendRequest(FriendRequestDto friendRequestDto) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        DocumentReference dbRef = invitesCollection.document(friendRequestDto.getRequestId());
        dbRef.update("declined", friendRequestDto.getDeclined(), "processed", true)
                .addOnSuccessListener(success -> {
                    liveData.setValue(Resource.success("success"));
                }).addOnFailureListener(failure -> {
                    liveData.setValue(Resource.error(failure.getMessage(), null));
                });
        return liveData;
    }

    private FriendRequestDto setFriendRequestDto(UserInfoDto friendsInfo) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    private List<FriendRequestDto> toFriendRequestDtoList(List<DocumentSnapshot> documents) {
        List<FriendRequestDto> requests = new ArrayList<>();

        for (int i = 0; i < documents.size(); i++) {
            Boolean processed = (Boolean) documents.get(i).getData().get("processed");
            Boolean declined = (Boolean) documents.get(i).getData().get("declined");

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
}


