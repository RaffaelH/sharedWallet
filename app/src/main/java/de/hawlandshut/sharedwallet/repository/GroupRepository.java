package de.hawlandshut.sharedwallet.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.model.methods.IGroupMethods;

public class GroupRepository implements IGroupMethods {
    private final String TAG ="GroupRepository";

    private static GroupRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String GROUP_COLLECTION_NAME = "groups";
    private final String MEMBERS_FIELD ="memberIds";
    private final String GROUP_ID_FIELD ="groupId";

    private final String CREATED_FIELD ="created";
    private CollectionReference groupsCollection = db.collection(GROUP_COLLECTION_NAME);
    private MutableLiveData<Resource<List<GroupDto>>> getAllGroupsMutableLiveData = new MutableLiveData<>();
    private ListenerRegistration allGroupsListener;


    public static GroupRepository getInstance() {
        if(instance == null) {
            instance = new GroupRepository();
        }
        return instance;
    }

    @Override
    public LiveData<Resource<List<GroupDto>>> getAllGroups() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            allGroupsListener = groupsCollection.whereArrayContains(MEMBERS_FIELD, uid ).orderBy(CREATED_FIELD, Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {

                if(error != null){
                    getAllGroupsMutableLiveData.setValue(Resource.error(error.getMessage(),null));
                }
                if (value != null &&  !value.isEmpty()) {
                    List<DocumentSnapshot> documents = value.getDocuments();
                    List<GroupDto> groupDtoList = toGroupList(documents);
                    Log.d("groups",groupDtoList.toString());
                    getAllGroupsMutableLiveData.setValue(Resource.success(groupDtoList));
                } else {
                    getAllGroupsMutableLiveData.setValue(Resource.error("keine Dokumente",null));
                }
            });
        }

        return getAllGroupsMutableLiveData;
    }

    @Override
    public LiveData<Resource<GroupDto>> getGroupById(String groupId) {
        MutableLiveData<Resource<GroupDto>> getGroupByIdMutableLiveData = new MutableLiveData<>();
        groupsCollection.whereEqualTo(GROUP_ID_FIELD,groupId).get().addOnSuccessListener(success ->{

            if(success.getDocuments().get(0).exists()){
                DocumentSnapshot documentSnapshot = success.getDocuments().get(0);
                ArrayList<Map<String,String>> membersDocs = (ArrayList<Map<String,String>> ) documentSnapshot.getData().get("members");
                List<UserInfoDto> members = mapToList(membersDocs);
                GroupDto groupDto = new GroupDto(
                        (String) documentSnapshot.getData().get("groupId"),
                        (String) documentSnapshot.getData().get("title"),
                        members,
                        (List<String>) documentSnapshot.getData().get("memberIds"),
                        (String) documentSnapshot.getData().get("owner"),
                        (Long) documentSnapshot.getData().get("created")
                );
                getGroupByIdMutableLiveData.setValue(Resource.success(groupDto));
            }else{
                getGroupByIdMutableLiveData.setValue(Resource.error("Kein Dokument",null));
            }

        }).addOnFailureListener(failure -> {
            getGroupByIdMutableLiveData.setValue(Resource.error(failure.getMessage(),null));
        });
        return getGroupByIdMutableLiveData;
    }

    @Override
    public LiveData<Resource<String>> addGroup(GroupDto group) {
        MutableLiveData<Resource<String>> addGroupMutableLiveData = new MutableLiveData<>();
        groupsCollection.add(group).addOnSuccessListener(addGroupSuccess -> {
            addGroupSuccess.update("groupId",addGroupSuccess.getId());
            addGroupMutableLiveData.setValue(Resource.success("success"));
        }).addOnFailureListener( addGroupFailure -> {
            addGroupMutableLiveData.setValue(Resource.error(addGroupFailure.getMessage(),null));
        });
        return addGroupMutableLiveData;
    }

    @Override
    public LiveData<Resource<String>> updateMembers(String groupId,UserInfoDto newMember) {
        MutableLiveData<Resource<String>> updateGroupMutableLiveData = new MutableLiveData<>();
        Task<QuerySnapshot> query = groupsCollection.whereEqualTo(GROUP_ID_FIELD,groupId).get();
        query.addOnSuccessListener(success -> {
            success.getDocuments().get(0).getReference().update("members",
                    FieldValue.arrayUnion(newMember),"memberIds",FieldValue.arrayUnion(newMember.getUserId())).addOnSuccessListener(updateSuccess ->{
                updateGroupMutableLiveData.setValue(Resource.success("success"));
            }).addOnFailureListener(failure -> {
                updateGroupMutableLiveData.setValue(Resource.error(failure.getMessage(),null));
            });
        }).addOnFailureListener(failure -> {
            updateGroupMutableLiveData.setValue(Resource.error(failure.getMessage(),null));
        }).addOnCanceledListener(()->{
            updateGroupMutableLiveData.setValue(Resource.error("Anfrage Abgebrochen",null));
        });

        return updateGroupMutableLiveData;
    }

    @Override
    public void removeListener(){
        if(allGroupsListener != null){
            allGroupsListener.remove();

        }
    }

    public void setGetAllGroupsMutableLiveData(MutableLiveData<Resource<List<GroupDto>>> getAllGroupsMutableLiveData) {
        this.getAllGroupsMutableLiveData = getAllGroupsMutableLiveData;
    }

    private List<GroupDto> toGroupList(List<DocumentSnapshot> documents){
        List<GroupDto> groups = new ArrayList<>();

        for(int i =0; i < documents.size();i++){
            ArrayList<Map<String,String>> membersDocs = (ArrayList<Map<String,String>> ) documents.get(i).getData().get("members");
            List<UserInfoDto> members = mapToList(membersDocs);
            GroupDto groupDto = new GroupDto(
                    (String) documents.get(i).getData().get("groupId"),
                    (String) documents.get(i).getData().get("title"),
                    members,
                    (List<String>)  documents.get(i).getData().get("memberIds"),
                    (String) documents.get(i).getData().get("owner"),
                    (Long) documents.get(i).getData().get("created")
            );
            groups.add(groupDto);
        }
        return groups;
    }

    private List<UserInfoDto> mapToList(ArrayList<Map<String,String>> membersDocs){
        List<UserInfoDto> members = new ArrayList<>();

        for(int i = 0; i< membersDocs.size();i++) {
            String displayName = "";
            String userId = "";
            for (Map.Entry<String, String> entry : membersDocs.get(i).entrySet()) {

                if (entry.getKey().equals("displayName")) {
                    displayName = entry.getValue();
                }
                if (entry.getKey().equals("userId")) {
                    userId = entry.getValue();
                }
            }
            UserInfoDto friend = new UserInfoDto(
                    displayName,
                    userId
            );
            members.add(friend);
        }
        return members;
    }

}
