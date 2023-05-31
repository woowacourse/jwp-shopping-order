package cart.dto;

public class MemberShowCurrentCashResponse {

    private int totalCash;

    private MemberShowCurrentCashResponse() {
    }

    public MemberShowCurrentCashResponse(int totalCash) {
        this.totalCash = totalCash;
    }

    public int getTotalCash() {
        return totalCash;
    }
}
