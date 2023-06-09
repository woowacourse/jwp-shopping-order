package cart.domain;

public class Order {
    private final Long id;
    private final Long memberId;
    private final int actualPrice;
    private final int originalPrice;
    private final int deliveryFee;

    public Order(Long memberId, int actualPrice, int originalPrice, int deliveryFee) {
        this(null, memberId, actualPrice, originalPrice, deliveryFee);
    }

    public Order(Long id, Long memberId, int actualPrice, int originalPrice, int deliveryFee) {
        this.id = id;
        this.memberId = memberId;
        this.actualPrice = actualPrice;
        this.originalPrice = originalPrice;
        this.deliveryFee = deliveryFee;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getActualPrice() {
        return actualPrice;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
