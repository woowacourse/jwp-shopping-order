package cart.dto;

import javax.validation.constraints.NotNull;

public class CouponRequest {
    @NotNull
    private Long id;

    private CouponRequest() {
    }

    public CouponRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
