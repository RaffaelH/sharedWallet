package de.hawlandshut.sharedwallet.model.methods;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;

public interface IGroupMethods {

    LiveData<Resource<List<GroupDto>>> getAllGroups();

    LiveData<Resource<GroupDto>> getGroupById(String groupId);

    LiveData<Resource<String>> addGroup(GroupDto group);

    LiveData<Resource<String>> updateMembers(String groupId, UserInfoDto newMember);

    LiveData<Resource<String>> deleteGroup(String groupId);

    void removeListener();

}
