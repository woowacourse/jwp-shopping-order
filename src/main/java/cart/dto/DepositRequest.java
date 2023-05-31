package cart.dto;

import javax.validation.constraints.NotNull;

public class DepositRequest {

    @NotNull(message = "충전할 금액을 입력하세요")
    private Long cashToCharge;

    private DepositRequest() {
    }

    private DepositRequest(final Long castToCharge) {
        this.cashToCharge = castToCharge;
    }

    public static DepositRequest from(final Long cash) {
        return new DepositRequest(cash);
    }

    public Long getCashToCharge() {
        return cashToCharge;
    }
}
