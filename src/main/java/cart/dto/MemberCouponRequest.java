package cart.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MemberCouponRequest {
    private final Long id;

    @JsonCreator
    public MemberCouponRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
