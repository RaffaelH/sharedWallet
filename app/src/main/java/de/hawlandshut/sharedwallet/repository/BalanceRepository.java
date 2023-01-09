package de.hawlandshut.sharedwallet.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import de.hawlandshut.sharedwallet.model.entities.BalanceDto;
import de.hawlandshut.sharedwallet.model.methods.IBalanceMethods;
import de.hawlandshut.sharedwallet.model.retro.Resource;

public class BalanceRepository implements IBalanceMethods {

    private static BalanceRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String BALANCE_COLLECTION_NAME = "balance";
    private final String GROUP_ID_FIELD ="groupId";
    private final String USER_ID_FIELD ="userId";
    private CollectionReference balanceCollection = db.collection(BALANCE_COLLECTION_NAME);
    private MutableLiveData<Resource<BalanceDto>> balanceLiveData = new MutableLiveData<>();
    private ListenerRegistration balanceListener;

    public static BalanceRepository getInstance() {
        if (instance == null) {
            instance = new BalanceRepository();
        }
        return instance;
    }

    @Override
    public LiveData<Resource<BalanceDto>> getBalance(String groupId) {
        balanceListener = balanceCollection.whereEqualTo(GROUP_ID_FIELD,groupId)
                .whereEqualTo(USER_ID_FIELD, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener((value, error) -> {
            if (error != null) {
                balanceLiveData.setValue(Resource.error(error.getMessage(), null));
            }
            if (value != null && !value.isEmpty()) {
                balanceLiveData.setValue(Resource.success(toBalanceDto(value.getDocuments().get(0))));
            }
        });
        return balanceLiveData;
    }

    @Override
    public LiveData<Resource<String>> resetBalance(String groupId) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
       Task<QuerySnapshot> query = balanceCollection.whereEqualTo(GROUP_ID_FIELD,groupId)
                .whereEqualTo(USER_ID_FIELD,FirebaseAuth.getInstance().getCurrentUser().getUid()).get();

        query.addOnSuccessListener(success -> {
            success.getDocuments().get(0).getReference().update("amount",0).addOnSuccessListener(updateSuccess ->{
                liveData.setValue(Resource.success("success"));
            }).addOnFailureListener(updateFailed ->{
                liveData.setValue(Resource.error(updateFailed.getMessage(),null));
            });
        });

        return liveData;
    }

    @Override
    public void removeListener() {
        if(balanceListener!=null){
            balanceListener.remove();
            balanceLiveData = new MutableLiveData<>();
        }
    }

    private BalanceDto toBalanceDto(DocumentSnapshot doc) {
        BalanceDto balance = new BalanceDto(
                (String) doc.getData().get("groupId"),
                (String) doc.getData().get("userId"),
                Double.valueOf(doc.getData().get("amount").toString())
        );
        return balance;
    }
}
