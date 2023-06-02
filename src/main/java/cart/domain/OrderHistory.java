package cart.domain;

public class OrderHistory {

    private Long id;
    private int originalPrice;
    private int usedPoint;
    private int orderPrice;
    private final Member member;


    public OrderHistory(final int originalPrice, final int usedPoint, final int orderPrice, final Member member) {
        this(null, originalPrice, usedPoint, orderPrice, member);
    }

    public OrderHistory(final Long id, final int originalPrice, final int usedPoint, final int orderPrice,
                        final Member member) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.orderPrice = orderPrice;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public Member getMember() {
        return member;
    }
}
