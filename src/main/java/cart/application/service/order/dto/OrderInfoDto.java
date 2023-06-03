package cart.application.service.order.dto;

import java.time.LocalDateTime;

public class OrderInfoDto {

    private final Long id;
    private final Long memberId;
    private final int totalPrice;
    private final int paymentPrice;
    private final int point;
    private final String createdAt;

    public OrderInfoDto(Long id, Long memberId, int totalPrice, int paymentPrice, int point, String createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.paymentPrice = paymentPrice;
        this.point = point;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }

    public int getPoint() {
        return point;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
