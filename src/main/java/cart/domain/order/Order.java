package cart.domain.order;


public class Order {

    private final Long id;
    private final Long MemberId;
    private final int totalPrice;

    public Order(Long memberId, int totalPrice) {
        this(null, memberId, totalPrice);
    }

    public Order(Long id, Long memberId, int totalPrice) {
        this.id = id;
        MemberId = memberId;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return MemberId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
