package cart.dao.entity;

public class OrdersEntity {
    private final long id;
    private final long memberId;
    private final int originalPrice;
    private final int discountPrice;
    private final boolean confirmState;

    public OrdersEntity(long id, long memberId, int originalPrice, int discountPrice, boolean confirmState) {
        this.id = id;
        this.memberId = memberId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.confirmState = confirmState;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public boolean isConfirmState() {
        return confirmState;
    }
}
