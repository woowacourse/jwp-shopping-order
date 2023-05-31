package cart.dao.entity;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Long shippingFee;
    private final Long totalPrice;
    private final String createdAt;

    public OrderEntity(Long memberId, Long shippingFee, Long totalPrice) {
        this(null, memberId, shippingFee, totalPrice,null);
    }

    public OrderEntity(Long id, Long memberId, Long shippingFee, Long totalPrice,String createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getShippingFee() {
        return shippingFee;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
