package cart.dto;

import javax.validation.constraints.NotNull;

public class WithdrawRequest {

    @NotNull(message = "충전할 금액을 입력하세요")
    private Long point;

    public WithdrawRequest() {
    }

    public WithdrawRequest(final Long point) {
        this.point = point;
    }

    public Long getPoint() {
        return point;
    }
}
