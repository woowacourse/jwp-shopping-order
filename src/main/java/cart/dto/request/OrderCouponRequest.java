package cart.dto.request;

public class OrderCouponRequest {

    private Long id;

    public OrderCouponRequest() {
    }

    public OrderCouponRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
