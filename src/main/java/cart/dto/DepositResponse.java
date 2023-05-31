package cart.dto;

public class DepositResponse {

    private final Long cash;

    private DepositResponse(final Long cash) {
        this.cash = cash;
    }

    public static DepositResponse from(final Long cash) {
        return new DepositResponse(cash);
    }

    public Long getCash() {
        return cash;
    }
}
