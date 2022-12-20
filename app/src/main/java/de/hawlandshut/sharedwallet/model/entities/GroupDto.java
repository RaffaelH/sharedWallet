package de.hawlandshut.sharedwallet.model.entities;

import java.util.List;

public class GroupDto {
    private String groupId;
    private String title;
    private List<String> memberNames;
    private List<String> members;
    private String owner;
    private Long created;

    public GroupDto(String groupId, String title, List<String> memberNames, List<String> memberId, String owner, Long  created) {
        this.groupId = groupId;
        this.title = title;
        this.memberNames = memberNames;
        this.members = memberId;
        this.owner = owner;
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

    public List<String> getMembers() {
        return members;
    }

    public String getOwner() {
        return owner;
    }

    public Long getCreated() {
        return created;
    }

}
