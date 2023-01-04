package de.hawlandshut.sharedwallet.model.entities;

import java.util.HashMap;

public class TransactionDto {

    private String transactionId;
    private String groupId;
    private String description;
    private String creditorId;
    private String creditor;
    private HashMap<String,Double> debtors;
    private double amount;
    private Long created;

    public TransactionDto(String transactionId, String groupId,String description, String creditorId, String creditor, HashMap<String, Double> debtors, double amount, Long created) {
        this.transactionId = transactionId;
        this.groupId = groupId;
        this.description = description;
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

    public String getTransactionId() {
        return transactionId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getDescription() {
        return description;
    }


}
