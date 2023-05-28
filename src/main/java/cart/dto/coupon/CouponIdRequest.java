package cart.dto.coupon;

public class CouponIdRequest {

    private Long id;

    public CouponIdRequest() {

    }

    public CouponIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
