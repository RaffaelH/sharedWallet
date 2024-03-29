package de.hawlandshut.sharedwallet.model.methods;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.model.entities.TransactionDto;

/**
 * Interface to define the methods that are used in the TransactionRepository and TransactionViewModel
 */
public interface ITransactionMethods {

    LiveData<Resource<List<TransactionDto>>> getAllTransactions(String groupId);

    LiveData<Resource<String>> addTransaction(TransactionDto transactionDto);

    void removeListener();

}
