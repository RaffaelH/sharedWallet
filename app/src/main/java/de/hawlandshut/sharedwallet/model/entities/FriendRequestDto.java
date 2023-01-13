package de.hawlandshut.sharedwallet.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * FriendRequestDto is a Data Object to define an Invitation from One User to another.
 */
public class FriendRequestDto implements Parcelable {

    private String requestId;
    private String inviterId;
    private String inviterName;
    private String invitedId;
    private String invitedName;
    private Boolean processed;
    private Boolean declined;
    private Long created;

    public static final Parcelable.Creator<FriendRequestDto> CREATOR = new Parcelable.Creator<FriendRequestDto>() {
        public FriendRequestDto createFromParcel(Parcel in) {
            return new FriendRequestDto(in);
        }

        public FriendRequestDto[] newArray(int size) {
            return new FriendRequestDto[size];
        }
    };

    private FriendRequestDto(Parcel in) {
        requestId = in.readString();
        inviterId = in.readString();
        inviterName = in.readString();
        invitedId = in.readString();
        invitedName = in.readString();
        processed = in.readByte()!=0;
        declined = in.readByte()!=0;
        created = in.readLong();
    }

    public FriendRequestDto(String requestId,String inviterId, String inviterName, String invitedId, String invitedName, boolean processed, boolean declined, Long created) {
        this.requestId = requestId;
        this.inviterId = inviterId;
        this.inviterName = inviterName;
        this.invitedId = invitedId;
        this.invitedName = invitedName;
        this.processed = processed;
        this.declined = declined;
        this.created = created;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(requestId);
        parcel.writeString(inviterId);
        parcel.writeString(inviterName);
        parcel.writeString(invitedId);
        parcel.writeByte((byte) (processed ? 1 : 0));
        parcel.writeByte((byte) (declined ? 1 : 0));
        parcel.writeLong(created);
    }

    public String getRequestId(){return requestId;}

    public String getInviterId() {
        return inviterId;
    }

    public String getInviterName() {
        return inviterName;
    }

    public String getInvitedId() {
        return invitedId;
    }

    public String getInvitedName(){return invitedName;}

    public boolean getProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed){
        this.processed = processed;
    }

    public boolean getDeclined() {
        return declined;
    }

    public void setDeclined(boolean declined){
        this.declined = declined;
    }

    public Long getCreated() {
        return created;
    }

}
