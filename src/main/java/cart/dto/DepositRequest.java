package cart.dto;

import javax.validation.constraints.NotNull;

public class DepositRequest {

    @NotNull(message = "충전할 금액을 입력하세요")
    private Long point;

    private DepositRequest() {
    }

    private DepositRequest(final Long point) {
        this.point = point;
    }

    public static DepositRequest from(final Long point) {
        return new DepositRequest(point);
    }

    public Long getPoint() {
        return point;
    }
}
