package de.hawlandshut.sharedwallet.model.entities;

import java.util.List;

/**
 * TransactionDto is a Data Object are used to define the amount, debtors and the creditor for a transaction.
 * Every transaction needs a reference to a group.
 */
public class TransactionDto {

    private String transactionId;
    private String groupId;
    private String description;
    private String creditorId;
    private String creditor;
    private List<String> debtors;
    private Double amount;
    private Long created;

    public TransactionDto(String transactionId, String groupId,String description, String creditorId, String creditor, List<String> debtors, Double amount, Long created) {
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

    public List<String> getDebtors() {
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
