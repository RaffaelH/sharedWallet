package de.hawlandshut.sharedwallet.model.methods;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.model.entities.TransactionDto;

public interface ITransactionMethods {

    LiveData<Resource<List<TransactionDto>>> getAllTransactions(String groupId);

    LiveData<Resource<TransactionDto>> getTransaction(String transactionId);

    LiveData<Resource<String>> deleteTransaction(String transactionId);

    void removeListener();

}
