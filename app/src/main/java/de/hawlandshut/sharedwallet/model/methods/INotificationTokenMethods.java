package de.hawlandshut.sharedwallet.model.methods;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import de.hawlandshut.sharedwallet.model.retro.Resource;

public interface INotificationTokenMethods {

    void onRefreshToken();

   LiveData<Resource<String>> getToken();

    void onNewToken(@NonNull String token);


}
