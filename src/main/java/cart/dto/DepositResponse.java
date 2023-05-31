package cart.dto;

public class DepositResponse {

    private final Long totalCash;

    private DepositResponse(final Long totalCash) {
        this.totalCash = totalCash;
    }

    public static DepositResponse from(final Long cash) {
        return new DepositResponse(cash);
    }

    public Long getTotalCash() {
        return totalCash;
    }
}
