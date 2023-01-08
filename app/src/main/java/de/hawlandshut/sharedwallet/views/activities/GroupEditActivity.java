package de.hawlandshut.sharedwallet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.util.List;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.viewmodel.BalanceViewModel;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.viewmodel.TransactionViewModel;
import de.hawlandshut.sharedwallet.views.components.LoadingDialog;
import de.hawlandshut.sharedwallet.views.components.SearchFriendListAdapter;
import de.hawlandshut.sharedwallet.views.components.TransactionListAdapter;

public class GroupEditActivity extends AppCompatActivity implements View.OnClickListener {

    private LoadingDialog mLoadingDialog;
    private TextView mTvTitle;
    private TextView mTvMembers;
    private TextView mTvErrorMsg;
    private TextView mTvUserBalance;
    private Button mBtnAddFriend;
    private Button mBtnAddTransaction;
    private GroupDto mGroup;
    private GroupViewModel mGroupViewModel;
    private TransactionViewModel mTransactionViewModel;
    private BalanceViewModel mBalanceViewModel;
    private TransactionListAdapter adapter = new TransactionListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);
        mLoadingDialog = new LoadingDialog(this);
        mTvTitle = findViewById(R.id.tv_group_edit_title);
        mTvMembers = findViewById(R.id.tv_group_edit_members);
        mTvUserBalance = findViewById(R.id.tv_user_balance);
        mBtnAddFriend = findViewById(R.id.btn_add_friend);
        mBtnAddTransaction = findViewById(R.id.flt_btn_new_transaction);
        mBtnAddTransaction.setOnClickListener(this);
        mBtnAddFriend.setOnClickListener(this);
        mGroup = getIntent().getParcelableExtra("groupDto");
        mGroupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        mTransactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        mBalanceViewModel = new ViewModelProvider(this).get(BalanceViewModel.class);
        mTvTitle.setText(mGroup.getTitle());
        mTvMembers.setText(setMembers(mGroup.getMembers()));
        RecyclerView recyclerView = findViewById(R.id.rv_transaction_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        mBalanceViewModel.getBalance(mGroup.getGroupId()).observe(this, balance ->{
            switch (balance.status){
                case SUCCESS:
                    mTvUserBalance.setText(balance.data.getAmount().toString());
            }
        });

        mTransactionViewModel.getAllTransactions(mGroup.getGroupId()).observe(this,transactions -> {
            switch(transactions.status){
                case SUCCESS:
                    adapter.setTransactions(transactions.data);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGroupViewModel.getGroupById(mGroup.getGroupId()).observe(this, result -> {
            switch(result.status){
                case SUCCESS:
                    mGroup = result.data;
                    mTvMembers.setText(setMembers(mGroup.getMembers()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTransactionViewModel.removeListener();
        mBalanceViewModel.removeListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_friend:
                startActivity(new Intent(this, AddFriendToGroupActivity.class)
                        .putExtra("groupDto", (Parcelable) mGroup));
                break;
            case R.id.flt_btn_new_transaction:
                if(mGroup.getMemberIds().size() < 2){
                    Toast.makeText(getApplicationContext(), "Lade Freunde ein.", Toast.LENGTH_LONG).show();
                }else{
                    startActivity(new Intent(this,AddTransactionActivity.class)
                            .putExtra("groupDto", (Parcelable) mGroup));
                }
        }
    }

    private String setMembers (List<UserInfoDto> members){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < members.size(); i++) {
            stringBuffer.append(members.get(i).getDisplayName());
            stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }

}

