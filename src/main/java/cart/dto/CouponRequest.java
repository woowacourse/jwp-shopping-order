package cart.dto;

public class CouponRequest {
    private Long id;

    private CouponRequest(){
    }

    public CouponRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
