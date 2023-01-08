package de.hawlandshut.sharedwallet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import de.hawlandshut.sharedwallet.model.entities.BalanceDto;
import de.hawlandshut.sharedwallet.model.methods.IBalanceMethods;
import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.repository.BalanceRepository;

public class BalanceViewModel extends AndroidViewModel implements IBalanceMethods {

    private BalanceRepository balanceRepository;

    public BalanceViewModel(@NonNull Application application) {
        super(application);
        balanceRepository = BalanceRepository.getInstance();
    }


    @Override
    public LiveData<Resource<BalanceDto>> getBalance(String groupId) {
        return balanceRepository.getBalance(groupId);
    }

    @Override
    public void removeListener() {
        balanceRepository.removeListener();
    }
}
