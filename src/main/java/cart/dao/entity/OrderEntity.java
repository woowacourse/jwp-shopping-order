package cart.dao.entity;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Long shippingFee;
    private final Long totalProductPrice;
    private final Long usedPoint;
    private final String createdAt;

    public OrderEntity(Long memberId, Long shippingFee, Long totalProductPrice, Long usedPoint) {
        this(null, memberId, shippingFee, totalProductPrice, usedPoint, null);
    }

    public OrderEntity(Long id, Long memberId, Long shippingFee, Long totalProductPrice, Long usedPoint, String createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.shippingFee = shippingFee;
        this.totalProductPrice = totalProductPrice;
        this.usedPoint = usedPoint;
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

    public Long getTotalProductPrice() {
        return totalProductPrice;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
