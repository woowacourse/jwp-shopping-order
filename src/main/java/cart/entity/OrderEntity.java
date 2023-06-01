package cart.entity;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Integer originalPrice;
    private final Integer discountPrice;
    private final Boolean confirmState;

    public OrderEntity(Long memberId, Integer originalPrice, Integer discountPrice) {
        this(null, memberId, originalPrice, discountPrice, false);
    }

    public OrderEntity(Long id, Long memberId, Integer originalPrice, Integer discountPrice, Boolean confirmState) {
        this.id = id;
        this.memberId = memberId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.confirmState = confirmState;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Boolean getConfirmState() {
        return confirmState;
    }
}
