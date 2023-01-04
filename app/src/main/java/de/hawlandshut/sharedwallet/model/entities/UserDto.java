package de.hawlandshut.sharedwallet.model.entities;

import java.util.List;

public class UserDto {

    private String userId;
    private String email;
    private String displayName;
    private String token;
    private List<UserInfoDto> friends;

    public UserDto(String userId, String email, String displayName, String token, List<UserInfoDto> friends) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.token = token;
        this.friends = friends;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken(){return token;}

    public List<UserInfoDto> getFriends() {
        return friends;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

}
