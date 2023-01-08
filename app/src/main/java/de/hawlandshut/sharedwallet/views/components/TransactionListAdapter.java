package de.hawlandshut.sharedwallet.views.components;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.TransactionDto;
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionHolder> {

    private List<TransactionDto> transactions = new ArrayList<>();

    @NonNull
    @Override
    public TransactionListAdapter.TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate((R.layout.transaction_item), parent, false);
        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
       TransactionDto transactionDto = transactions.get(position);
       Log.d("Adapter",transactionDto.getCreated().toString());
       String created= new Date(transactionDto.getCreated().longValue()).toLocaleString();
       holder.tvCreated.setText(created);
       holder.tvCreditor.setText(transactionDto.getCreditor());
       holder.tvAmount.setText(String.valueOf(transactionDto.getAmount()));
       holder.tvTitle.setText(transactionDto.getDescription());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setTransactions(List<TransactionDto> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
        private TextView tvCreated;
        private TextView tvAmount;
        private TextView tvCreditor;
        private TextView tvTitle;

        public TransactionHolder(View itemView) {
            super(itemView);
            tvCreated = itemView.findViewById(R.id.tv_transaction_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCreditor = itemView.findViewById(R.id.tv_creditor);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}


