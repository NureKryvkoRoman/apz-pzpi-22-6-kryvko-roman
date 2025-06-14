package ua.nure.kryvko.roman.apz.subscription;

import java.time.LocalDateTime;

public class SubscriptionResponse {
    Integer id;
    Integer userId;
    LocalDateTime startDate;
    LocalDateTime endDate;
    SubscriptionStatus status;

    public SubscriptionResponse() {}

    public SubscriptionResponse(Integer id, Integer userId, LocalDateTime startDate,
                                LocalDateTime endDate, SubscriptionStatus status) {
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

}
