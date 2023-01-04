package de.hawlandshut.sharedwallet.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import de.hawlandshut.sharedwallet.model.methods.INotificationTokenMethods;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.service.PushNotificationService;

public class NotificationTokenRepository implements INotificationTokenMethods {

    private static NotificationTokenRepository instance;

    public static NotificationTokenRepository getInstance(){
        if(instance == null){
            instance = new NotificationTokenRepository();
        }
        return instance;
    }

    @Override
    public void onRefreshToken() {

    }

    @Override
    public LiveData<Resource<String>> getToken() {

        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                         liveData.setValue(Resource.error(task.getException().getMessage(),null));
                        }
                        // Get new FCM registration token
                     liveData.setValue(Resource.success(task.getResult()));
                    }
                });
        return liveData;
    }

    @Override
    public void onNewToken(@NonNull String token) {

    }
}
