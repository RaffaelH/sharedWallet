package de.hawlandshut.sharedwallet.model.entities;

public class BalanceDto {

    private String groupId;
    private String userId;
    private Double amount;

    public BalanceDto(String groupId, String userId, Double amount){
        this.groupId = groupId;
        this.userId = userId;
        this.amount = amount;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }

    public Double getAmount() {
        return amount;
    }
}
