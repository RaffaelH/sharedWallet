package de.hawlandshut.sharedwallet.model.methods;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import de.hawlandshut.sharedwallet.model.retro.Resource;

public interface INotificationTokenMethods {

   LiveData<Resource<String>> getToken();

}
