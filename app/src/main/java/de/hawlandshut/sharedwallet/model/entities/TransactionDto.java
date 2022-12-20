package de.hawlandshut.sharedwallet.model.entities;

import java.util.Date;
import java.util.HashMap;

public class TransactionDto {

    private String creditorId;
    private String creditor;
    private HashMap<String,Double> debtors;
    private double amount;
    private Long created;

    public TransactionDto(String creditorId, String creditor, HashMap<String, Double> debtors, double amount, Long created) {
        this.creditorId = creditorId;
        this.creditor = creditor;
        this.debtors = debtors;
        this.amount = amount;
        this.created = created;
    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getCreditor() {
        return creditor;
    }

    public double getAmount() {
        return amount;
    }

    public Long getCreated() {
        return created;
    }

    public HashMap<String, Double> getDebtors() {
        return debtors;
    }



}
