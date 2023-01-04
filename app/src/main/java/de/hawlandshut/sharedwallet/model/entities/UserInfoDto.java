package de.hawlandshut.sharedwallet.model.entities;

public class UserInfoDto {

    private String displayName;
    private String userId;

    public UserInfoDto(String displayName, String userId) {
        this.displayName = displayName;
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserId() {
        return userId;
    }

}
