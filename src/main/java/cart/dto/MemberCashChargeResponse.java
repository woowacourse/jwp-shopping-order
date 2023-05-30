package cart.dto;

public class MemberCashChargeResponse {

    private int chargedCash;

    private MemberCashChargeResponse() {
    }

    public MemberCashChargeResponse(int chargedCash) {
        this.chargedCash = chargedCash;
    }

    public int getChargedCash() {
        return chargedCash;
    }
}
