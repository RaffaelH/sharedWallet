package de.hawlandshut.sharedwallet.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GroupDto implements Parcelable {
    private String groupId;
    private String title;
    private List<String> memberNames;
    private List<String> members;
    private String owner;
    private Long created;

    public static final Parcelable.Creator<GroupDto> CREATOR = new Parcelable.Creator<GroupDto>() {
        public GroupDto createFromParcel(Parcel in) {
            return new GroupDto(in);
        }
        public GroupDto[] newArray(int size) {
            return new GroupDto[size];
        }
    };

    public GroupDto(String groupId, String title, List<String> memberNames, List<String> memberId, String owner, Long  created) {
        this.groupId = groupId;
        this.title = title;
        this.memberNames = memberNames;
        this.members = memberId;
        this.owner = owner;
        this.created = created;
    }

    private GroupDto(Parcel in) {
        groupId = in.readString();
        title = in.readString();
        memberNames = in.readArrayList(ClassLoader.getSystemClassLoader());
        members = in.readArrayList(ClassLoader.getSystemClassLoader());
        owner = in.readString();
        created = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(groupId);
            parcel.writeString(title);
            parcel.writeList(memberNames);
            parcel.writeList(members);
            parcel.writeString(owner);
            parcel.writeLong(created);
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
