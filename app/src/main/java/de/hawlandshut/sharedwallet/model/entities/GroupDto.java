package de.hawlandshut.sharedwallet.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * GroupDto is a Data Object to define a Group where Users can share their transactions.
 */
public class GroupDto implements Parcelable {
    private String groupId;
    private String title;
    private List<UserInfoDto> members;
    private List<String> memberIds;
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

    public GroupDto(String groupId, String title,List<UserInfoDto> members, List<String> memberIds, String owner, Long  created) {
        this.groupId = groupId;
        this.title = title;
        this.members = members;
        this.memberIds = memberIds;
        this.owner = owner;
        this.created = created;
    }

    private GroupDto(Parcel in) {
        groupId = in.readString();
        title = in.readString();
       members = new ArrayList<>();
       in.readTypedList(members,UserInfoDto.CREATOR);
        memberIds =  in.readArrayList(ClassLoader.getSystemClassLoader());
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
            parcel.writeTypedList(members);
            parcel.writeList(memberIds);
            parcel.writeString(owner);
            parcel.writeLong(created);
    }

    public String getGroupId() {
        return groupId;
    }

    public String getTitle() {
        return title;
    }

    public List<UserInfoDto> getMembers() {
        return members;
    }

    public List<String> getMemberIds(){return memberIds;}

    public String getOwner() {
        return owner;
    }

    public Long getCreated() {
        return created;
    }

}
