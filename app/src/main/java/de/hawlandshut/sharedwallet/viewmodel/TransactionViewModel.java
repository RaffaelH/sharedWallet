package de.hawlandshut.sharedwallet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.model.entities.TransactionDto;
import de.hawlandshut.sharedwallet.model.methods.ITransactionMethods;
import de.hawlandshut.sharedwallet.repository.TransactionRepository;

public class TransactionViewModel extends AndroidViewModel implements ITransactionMethods {

    private TransactionRepository transactionRepository;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        transactionRepository = TransactionRepository.getInstance();
    }

    @Override
    public LiveData<Resource<List<TransactionDto>>> getAllTransactions(String groupId) {
        return transactionRepository.getAllTransactions(groupId);
    }

    @Override
    public LiveData<Resource<TransactionDto>> getTransaction(String transactionId) {
        return null;
    }

    @Override
    public LiveData<Resource<String>> addTransaction(TransactionDto transactionDto) {
        return transactionRepository.addTransaction(transactionDto);
    }

    @Override
    public LiveData<Resource<String>> deleteTransaction(String transactionId) {
        return null;
    }

    @Override
    public void removeListener() {

    }
}
