package de.hawlandshut.sharedwallet.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;
import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;
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
                Log.d("Group",success.getDocuments().get(0).toString());
                GroupDto groupDto = new GroupDto(
                        (String) documentSnapshot.getData().get("groupId"),
                        (String) documentSnapshot.getData().get("title"),
                        (List<String>)documentSnapshot.getData().get("memberNames"),
                        (List<String>)documentSnapshot.getData().get("members"),
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
        groupsCollection.document(groupId).delete().addOnSuccessListener(groupDeleteSuccess ->{
            deleteGroupMutableLiveData.setValue(Resource.success("success"));
        }).addOnFailureListener(groupDeleteFailure -> {
            deleteGroupMutableLiveData.setValue(Resource.error(groupDeleteFailure.getMessage(),null));
        });

        return deleteGroupMutableLiveData;
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
            GroupDto groupDto = new GroupDto(
                    (String) documents.get(i).getData().get("groupId"),
                    (String) documents.get(i).getData().get("title"),
                    (List<String>)documents.get(i).getData().get("memberNames"),
                    (List<String>)documents.get(i).getData().get("members"),
                    (String) documents.get(i).getData().get("owner"),
                    (Long) documents.get(i).getData().get("created")

            );
            groups.add(groupDto);
        }
        return groups;
    }

}
