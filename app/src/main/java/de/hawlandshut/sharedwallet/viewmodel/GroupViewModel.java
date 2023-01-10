package de.hawlandshut.sharedwallet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.model.methods.IGroupMethods;
import de.hawlandshut.sharedwallet.repository.GroupRepository;

public class GroupViewModel extends AndroidViewModel implements IGroupMethods {

    private GroupRepository groupRepository;
    private LiveData<Resource<List<GroupDto>>> allGroups;

    public GroupViewModel(@NonNull Application application) {
        super(application);
        groupRepository = GroupRepository.getInstance();
        allGroups = groupRepository.getAllGroups();
    }

    @Override
    public LiveData<Resource<List<GroupDto>>> getAllGroups() {
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
    public LiveData<Resource<String>> updateMembers(String groupId, UserInfoDto newMember) {
        return groupRepository.updateMembers(groupId,newMember);
    }

    @Override
    public void removeListener(){
        groupRepository.removeListener();
    }

    public void resetAllGroups(){
        groupRepository.setGetAllGroupsMutableLiveData(new MutableLiveData<>());
        allGroups = groupRepository.getAllGroups();
    }

}
