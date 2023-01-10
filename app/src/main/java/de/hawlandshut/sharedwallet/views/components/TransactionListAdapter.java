package de.hawlandshut.sharedwallet.views.components;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.Auth;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.TransactionDto;

import de.hawlandshut.sharedwallet.views.activities.GroupEditActivity;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionHolder> {

    private List<TransactionDto> transactions = new ArrayList<>();
    private final Locale LOCAL = new Locale("de", "DE");
    private Context context;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance(LOCAL);

    public TransactionListAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionListAdapter.TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate((R.layout.transaction_item), parent, false);
        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        TransactionDto transactionDto = transactions.get(position);
        String uid = ((GroupEditActivity)context).getCurrentUserId();
        Date date = new Date(transactionDto.getCreated().longValue());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, LOCAL);
        String created = dateFormat.format(date);
        holder.tvCreated.setText(created);
        holder.tvCreditor.setText(transactionDto.getCreditor());
        String rounded = formatter.format(transactionDto.getAmount());
        holder.tvAmount.setText(rounded);
        holder.tvTitle.setText(transactionDto.getDescription());
        if(transactionDto.getCreditorId().equals(uid) && !transactionDto.getDebtors().contains(uid) ){
            holder.tvCreditText.setText("Du bekommst :");
            double amount = transactionDto.getAmount() - transactionDto.getAmount() / (transactionDto.getDebtors().size() +1);
            String credit = formatter.format(amount);
            holder.tvCredit.setText(credit);
            holder.tvCredit.setTextColor(((GroupEditActivity)context).getColor(R.color.green));
        }if(transactionDto.getDebtors().contains(uid)){
            holder.tvCreditText.setText("Du schuldest :");
            double amount = transactionDto.getAmount() / (transactionDto.getDebtors().size() +1);
            String credit = formatter.format(amount);
            holder.tvCredit.setText(credit);
            holder.tvCredit.setTextColor(((GroupEditActivity)context).getColor(R.color.red));
        }if(!transactionDto.getDebtors().contains(uid) && !transactionDto.getCreditorId().equals(uid) ){
            holder.tvCreditText.setText("Ausgabe vor Gruppeneintritt.");
             holder.tvCredit.setText("");
        }
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
        private TextView tvCredit;
        private TextView tvCreditText;

        public TransactionHolder(View itemView) {
            super(itemView);
            tvCreated = itemView.findViewById(R.id.tv_transaction_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCreditor = itemView.findViewById(R.id.tv_creditor);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvCredit = itemView.findViewById(R.id.tv_credit);
            tvCreditText = itemView.findViewById(R.id.tv_credit_text);
        }
    }
}


