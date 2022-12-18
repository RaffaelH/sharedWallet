package de.hawlandshut.sharedwallet.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.GroupInfoDto;
import de.hawlandshut.sharedwallet.model.entities.Resource;
import de.hawlandshut.sharedwallet.model.methods.IGroupMethods;

public class GroupRepository implements IGroupMethods {
    private final String TAG ="GroupRepository";

    private static GroupRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String GROUP_COLLECTION_NAME = "groups";
    private final String GROUP_INFO_COLLECTION_NAME = "groupinfo";
    private final String MEMBERS_FIELD ="members";
    private final String GROUP_ID_FIELD ="groupId";
    private final String CREATED_FIELD ="created";
    private CollectionReference groupsCollection = db.collection(GROUP_COLLECTION_NAME);
    private CollectionReference groupInfoCollection = db.collection(GROUP_INFO_COLLECTION_NAME);
    private MutableLiveData<Resource<List<GroupInfoDto>>> getAllGroupsMutableLiveData = new MutableLiveData<>();

    public static GroupRepository getInstance() {
        if(instance == null) {
            instance = new GroupRepository();
        }
        return instance;
    }

    @Override
    public LiveData<Resource<List<GroupInfoDto>>> getAllGroups() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            groupInfoCollection.whereArrayContains(MEMBERS_FIELD, uid ).orderBy(CREATED_FIELD, Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
                if(error != null){
                    getAllGroupsMutableLiveData.setValue(Resource.error(error.getMessage(),null));
                }
                if (value != null &&  !value.isEmpty()) {
                    List<DocumentSnapshot> documents = value.getDocuments();
                    List<GroupInfoDto> groupInfoDtoList = toGroupList(documents);
                    Log.d("groups",groupInfoDtoList.toString());
                    getAllGroupsMutableLiveData.setValue(Resource.success(groupInfoDtoList));
                } else {
                    getAllGroupsMutableLiveData.setValue(Resource.error("kein Dokument",null));
                }
            });
        }

        return getAllGroupsMutableLiveData;
    }

    @Override
    public LiveData<Resource<GroupDto>> getGroupById(String groupId) {
        MutableLiveData<Resource<GroupDto>> getGroupByIdMutableLiveData = new MutableLiveData<>();
        groupsCollection.whereEqualTo(GROUP_ID_FIELD,groupId).get().addOnSuccessListener(getGroupByIdSuccess ->{
            if(getGroupByIdSuccess.getDocuments()!= null){
                DocumentSnapshot documentSnapshot = getGroupByIdSuccess.getDocuments().get(0);
                GroupDto groupDto = documentSnapshot.toObject(GroupDto.class);
                getGroupByIdMutableLiveData.setValue(Resource.success(groupDto));
            }
            else{
                getGroupByIdMutableLiveData.setValue(Resource.error("Kein Dokument",null));
            }
        }).addOnFailureListener(getGroupByIdFailure -> {
            getGroupByIdMutableLiveData.setValue(Resource.error(getGroupByIdFailure.getMessage(),null));
        });
        return getGroupByIdMutableLiveData;
    }

    @Override
    public LiveData<Resource<String>> addGroup(GroupDto group) {
        MutableLiveData<Resource<String>> addGroupMutableLiveData = new MutableLiveData<>();
        groupsCollection.add(group).addOnSuccessListener( addGroupSuccess -> {
            addGroupMutableLiveData.setValue(Resource.success("success"));
        }).addOnFailureListener( addGroupFailure -> {
            addGroupMutableLiveData.setValue(Resource.error(addGroupFailure.getMessage(),null));
        });
        return addGroupMutableLiveData;
    }

    @Override
    public LiveData<Resource<String>> updateGroup(String groupId,GroupDto group) {
        MutableLiveData<Resource<String>> updateGroupMutableLiveData = new MutableLiveData<>();

        groupsCollection.document(groupId).set(group).addOnSuccessListener(groupUpdateSuccess -> {
            updateGroupMutableLiveData.setValue(Resource.success("success"));
        }).addOnFailureListener(groupUpdateFailure -> {
            updateGroupMutableLiveData.setValue(Resource.error(groupUpdateFailure.getMessage(),null));
        });

        return updateGroupMutableLiveData;
    }

    @Override
    public LiveData<Resource<String>> deleteGroup(String groupId) {
        MutableLiveData<Resource<String>> deleteGroupMutableLiveData = new MutableLiveData<>();
        groupsCollection.document(groupId).delete().addOnSuccessListener(groupDelteSuccess ->{
            deleteGroupMutableLiveData.setValue(Resource.success("success"));
        }).addOnFailureListener(groupDeleteFailure -> {
            deleteGroupMutableLiveData.setValue(Resource.error(groupDeleteFailure.getMessage(),null));
        });

        return deleteGroupMutableLiveData;
    }

    private List<GroupInfoDto> toGroupList(List<DocumentSnapshot> documents){
        List<GroupInfoDto> groupInfoDtoList = new ArrayList<>();


        for(int i =0; documents.size() > i;i++){
            GroupInfoDto groupInfoDto = new GroupInfoDto(
                    (String) documents.get(i).getData().get("title"),
                    (String) documents.get(i).getData().get("groupId"),
                    (List<String>)documents.get(i).getData().get("memberNames"),
                    (List<String>)documents.get(i).getData().get("members"),
                    (Long) documents.get(i).getData().get("created")
            );

            groupInfoDtoList.add(groupInfoDto);
        }
        return groupInfoDtoList;
    }

}
