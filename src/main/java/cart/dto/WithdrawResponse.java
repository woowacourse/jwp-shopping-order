package cart.dto;

import cart.domain.Member;

public class WithdrawResponse {

    private final Long cash;

    private WithdrawResponse(final Long cash) {
        this.cash = cash;
    }

    public static WithdrawResponse from(final Long cash) {
        return new WithdrawResponse(cash);
    }

    public Long getCash() {
        return cash;
    }
}
