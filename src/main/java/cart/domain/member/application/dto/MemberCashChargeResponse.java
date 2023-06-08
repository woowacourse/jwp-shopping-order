package cart.domain.member.application.dto;

public class MemberCashChargeResponse {

    private int totalCash;

    private MemberCashChargeResponse() {
    }

    public MemberCashChargeResponse(int totalCash) {
        this.totalCash = totalCash;
    }

    public int getTotalCash() {
        return totalCash;
    }
}
