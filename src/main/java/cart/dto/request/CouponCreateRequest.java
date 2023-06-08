package cart.dto.request;

public class CouponCreateRequest {
    private Long id;

    public CouponCreateRequest() {
    }

    public CouponCreateRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
