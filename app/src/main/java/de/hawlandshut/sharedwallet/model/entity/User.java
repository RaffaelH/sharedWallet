package de.hawlandshut.sharedwallet.model.entity;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
import de.hawlandshut.sharedwallet.model.interace.IUser;

public class User implements Serializable, IUser {
    public String uid;
    public String name;
    @SuppressWarnings("WeakerAccess")
    public String email;
    @Exclude
    public boolean isAuthenticated;

    public User() {}

   public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }
}
