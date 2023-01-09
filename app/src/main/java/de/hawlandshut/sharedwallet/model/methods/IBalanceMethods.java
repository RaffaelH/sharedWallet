package de.hawlandshut.sharedwallet.model.methods;

import androidx.lifecycle.LiveData;

import de.hawlandshut.sharedwallet.model.entities.BalanceDto;
import de.hawlandshut.sharedwallet.model.retro.Resource;

public interface IBalanceMethods {

    LiveData<Resource<BalanceDto>> getBalance(String groupId);

    LiveData<Resource<String>> resetBalance(String groupId);

    void removeListener();
}
