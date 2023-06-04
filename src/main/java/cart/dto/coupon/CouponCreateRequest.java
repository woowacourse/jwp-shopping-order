package cart.dto.coupon;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CouponCreateRequest {

    @NotNull(message = "ID는 필수 입력입니다. 반드시 입력해주세요.")
    @Positive(message = "유효한 ID를 입력해주세요.")
    private Long id;

    public CouponCreateRequest() {
    }

    public CouponCreateRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
