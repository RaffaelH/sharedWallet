package de.hawlandshut.sharedwallet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import de.hawlandshut.sharedwallet.model.methods.INotificationTokenMethods;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.repository.NotificationTokenRepository;

public class NotificationTokenViewModel extends AndroidViewModel implements INotificationTokenMethods {

    private NotificationTokenRepository notificationTokenRepository;

    public NotificationTokenViewModel(@NonNull Application application) {
        super(application);
        notificationTokenRepository = NotificationTokenRepository.getInstance();
    }

    @Override
    public void onRefreshToken() {

    }

    @Override
    public LiveData<Resource<String>> getToken() {
        return notificationTokenRepository.getToken();
    }

    @Override
    public void onNewToken(@NonNull String token) {

    }
}
