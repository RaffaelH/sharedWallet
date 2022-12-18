package de.hawlandshut.sharedwallet.model.entities;

import java.util.Date;
import java.util.List;

public class GroupInfoDto {

    private String title;
    private String groupId;
    private List<String> memberNames;
    private List<String> members;
    private Long created;

    public GroupInfoDto(){}

    public GroupInfoDto(String title, String groupId, List<String> memberNames, List<String> members, Long created) {
        this.title = title;
        this.groupId = groupId;
        this.memberNames = memberNames;
        this.members = members;
        this.created = created;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }

    public Long getCreated() {
        return created;
    }
    public List<String> getMembers() {
        return members;
    }

}
