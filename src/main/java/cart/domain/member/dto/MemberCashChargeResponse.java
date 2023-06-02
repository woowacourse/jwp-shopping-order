package cart.domain.member.dto;

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
