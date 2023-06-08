package cart.domain.member.application.dto;

import javax.validation.constraints.Positive;

public class MemberCashChargeRequest {

    @Positive(message = "충전할 금액은 1원 이상이어야합니다.")
    private int cashToCharge;

    private MemberCashChargeRequest() {
    }

    public MemberCashChargeRequest(int cashToCharge) {
        this.cashToCharge = cashToCharge;
    }

    public int getCashToCharge() {
        return cashToCharge;
    }
}
