package cart.member.ui.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class DepositRequest {

    @NotNull(message = "충전할 금액을 입력하세요")
    @Positive(message = "충전 금액은 1원 이상이어야 합니다")
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
