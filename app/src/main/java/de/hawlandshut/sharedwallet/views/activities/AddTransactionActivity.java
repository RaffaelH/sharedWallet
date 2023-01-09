package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.TransactionDto;
import de.hawlandshut.sharedwallet.repository.AuthRepository;
import de.hawlandshut.sharedwallet.viewmodel.AuthViewModel;
import de.hawlandshut.sharedwallet.viewmodel.TransactionViewModel;

public class AddTransactionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdtTransactionDescription;
    private EditText mEdtAmount;
    private GroupDto mGroup;
    private Button mBtnCreateTransaction;
    private TransactionViewModel mTransactionViewModel;
    private AuthViewModel mAuthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        mEdtTransactionDescription = findViewById(R.id.et_transaction_title);
        mEdtAmount = findViewById(R.id.et_transaction_amount);
        mBtnCreateTransaction = findViewById(R.id.btn_create_transactions);
        mTransactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mBtnCreateTransaction.setOnClickListener(this);
        mGroup = getIntent().getParcelableExtra("groupDto");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_transactions:
                addTransaction();
                break;
        }
    }


    private void addTransaction() {
            List<String> debtors = mGroup.getMemberIds();

            debtors.remove( mAuthViewModel.getCurrentFirebaseUser().getUid());
            Log.d("addTransaction", String.valueOf(debtors.size()));

            TransactionDto transaction = new TransactionDto(
                    "",
                    mGroup.getGroupId(),
                    mEdtTransactionDescription.getText().toString(),
                    mAuthViewModel.getCurrentFirebaseUser().getUid(),
                    mAuthViewModel.getCurrentFirebaseUser().getDisplayName(),
                    debtors,
                    Double.valueOf(mEdtAmount.getText().toString()),
                    new Date().getTime()
                    );
            mTransactionViewModel.addTransaction(transaction).observe(this, transactionTask -> {
                switch(transactionTask.status){
                    case SUCCESS:
                        finish();
                    case ERROR:
                        Log.d("addTransaction", " "+ transactionTask.message);
                }
            });
    }
}