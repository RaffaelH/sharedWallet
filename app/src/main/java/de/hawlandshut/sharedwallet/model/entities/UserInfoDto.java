package de.hawlandshut.sharedwallet.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * UserInfoDto is a Data Object to define short Information about a User.
 */
public class UserInfoDto implements Parcelable {

    private String displayName;
    private String userId;

    public UserInfoDto(String displayName, String userId) {
        this.displayName = displayName;
        this.userId = userId;
    }

    public static final Parcelable.Creator<UserInfoDto> CREATOR = new Parcelable.Creator<UserInfoDto>() {
        public UserInfoDto createFromParcel(Parcel in) {
            return new UserInfoDto(in);
        }
        public UserInfoDto[] newArray(int size) {
            return new UserInfoDto[size];
        }
    };

    private UserInfoDto(Parcel in) {
        displayName = in.readString();
        userId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(displayName);
        parcel.writeString(userId);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
