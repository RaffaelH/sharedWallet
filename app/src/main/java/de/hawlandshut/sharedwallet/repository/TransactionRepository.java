package de.hawlandshut.sharedwallet.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hawlandshut.sharedwallet.model.retro.Resource;
import de.hawlandshut.sharedwallet.model.entities.TransactionDto;
import de.hawlandshut.sharedwallet.model.methods.ITransactionMethods;

public class TransactionRepository implements ITransactionMethods {

    private static TransactionRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TRANSACTION_COLLECTION_NAME = "transactions";
    private final String GROUP_ID_FIELD ="groupId";
    private final String TRANSACTION_FIELD ="transactionId";
    private final String CREATED_FIELD ="created";
    private CollectionReference transactionCollection = db.collection(TRANSACTION_COLLECTION_NAME);
    private MutableLiveData<Resource<List <TransactionDto>>> getAllTransactions = new MutableLiveData<>();
    private ListenerRegistration allTransactionsListener;

    public static TransactionRepository getInstance(){
        if(instance == null){
            instance  = new TransactionRepository();
        }
        return instance;
    }

    @Override
    public LiveData<Resource<List<TransactionDto>>> getAllTransactions(String groupId) {
        allTransactionsListener = transactionCollection.whereEqualTo(GROUP_ID_FIELD, groupId)
                .orderBy(CREATED_FIELD, Query.Direction.DESCENDING)
                .addSnapshotListener( (value,error) -> {
                    if(error != null){
                        getAllTransactions.setValue(Resource.error(error.getMessage(), null));
                    }
                    if(value != null && !value.isEmpty()){
                        getAllTransactions.setValue(Resource.success(setTransactionList(value.getDocuments())));
                    }
                    else{
                        getAllTransactions.setValue(Resource.error("keine Dokumente",null));
                    }
                });

        return getAllTransactions;
    }

    @Override
    public LiveData<Resource<TransactionDto>> getTransaction(String transactionId) {
        return null;
    }

    @Override
    public LiveData<Resource<String>> addTransaction(TransactionDto transactionDto) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();

        transactionCollection.add(transactionDto).addOnSuccessListener(success ->{
            liveData.setValue(Resource.success("success"));
        }).addOnFailureListener(failure -> {
            liveData.setValue(Resource.error(failure.getMessage(),null));
        });

        return liveData;
    }

    @Override
    public LiveData<Resource<String>> deleteTransaction(String transactionId) {
        return null;
    }

    @Override
    public void removeListener() {
        allTransactionsListener.remove();
        getAllTransactions = new MutableLiveData<>();
    }

    public void setTransactions(MutableLiveData<Resource<List<TransactionDto>>> newTransactions){
        getAllTransactions = newTransactions;
    }

    private List<TransactionDto> setTransactionList(List<DocumentSnapshot> documents){
        List<TransactionDto> transactionList = new ArrayList<>();

        for(int i = 0; documents.size() > i; i++){
            TransactionDto transaction = new TransactionDto(
                    (String) documents.get(i).getData().get("transactionId"),
                    (String) documents.get(i).getData().get("groupId"),
                    (String) documents.get(i).getData().get("description"),
                    (String) documents.get(i).getData().get("creditorId"),
                    (String) documents.get(i).getData().get("creditor"),
                    (List<String>) documents.get(i).getData().get("debtors"),
                    (Double) documents.get(i).get("amount"),
                    (Long) documents.get(i).getData().get("created")
            );
            transactionList.add(transaction);
        }
        return transactionList;
    }
}
