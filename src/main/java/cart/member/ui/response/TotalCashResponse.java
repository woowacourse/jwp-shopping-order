package cart.member.ui.response;

public class TotalCashResponse {

    private final Long totalCash;

    private TotalCashResponse(final Long totalCash) {
        this.totalCash = totalCash;
    }

    public static TotalCashResponse from(final Long totalCash) {
        return new TotalCashResponse(totalCash);
    }

    public Long getTotalCash() {
        return totalCash;
    }
}
