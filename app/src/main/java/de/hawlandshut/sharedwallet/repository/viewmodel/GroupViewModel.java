package de.hawlandshut.sharedwallet.repository.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.GroupInfoDto;
import de.hawlandshut.sharedwallet.model.entities.Resource;
import de.hawlandshut.sharedwallet.model.methods.IGroupMethods;
import de.hawlandshut.sharedwallet.repository.GroupRepository;

public class GroupViewModel extends AndroidViewModel implements IGroupMethods {

    private GroupRepository groupRepository;
    private LiveData<Resource<List<GroupInfoDto>>> allGroups;

    public GroupViewModel(@NonNull Application application) {
        super(application);
        groupRepository = GroupRepository.getInstance();
        allGroups = groupRepository.getAllGroups();
    }

    @Override
    public LiveData<Resource<List<GroupInfoDto>>> getAllGroups() {

        return allGroups;
    }

    @Override
    public LiveData<Resource<GroupDto>> getGroupById(String groupId) {
        return groupRepository.getGroupById(groupId);
    }

    @Override
    public LiveData<Resource<String>> addGroup(GroupDto group) {
        return groupRepository.addGroup(group);
    }

    @Override
    public LiveData<Resource<String>> updateGroup(String groupId,GroupDto group) {
        return groupRepository.updateGroup(groupId,group);
    }

    @Override
    public LiveData<Resource<String>> deleteGroup(String groupId) {
        return groupRepository.deleteGroup(groupId);
    }

    @Override
    public void removeListener(){
        groupRepository.removeListener();
    }

}
