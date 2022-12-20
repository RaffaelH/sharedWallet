package de.hawlandshut.sharedwallet.model.methods;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.GroupInfoDto;
import de.hawlandshut.sharedwallet.model.entities.Resource;

public interface IGroupMethods {

    LiveData<Resource<List<GroupInfoDto>>> getAllGroups();

    LiveData<Resource<GroupDto>> getGroupById(String groupId);

    LiveData<Resource<String>> addGroup(GroupDto group);

    LiveData<Resource<String>> updateGroup(String groupId, GroupDto group);

    LiveData<Resource<String>> deleteGroup(String groupId);

    void removeListener();

}
