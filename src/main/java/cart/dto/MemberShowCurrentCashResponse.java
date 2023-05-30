package cart.dto;

public class MemberShowCurrentCashResponse {

    private int currentCash;

    private MemberShowCurrentCashResponse() {
    }

    public MemberShowCurrentCashResponse(int currentCash) {
        this.currentCash = currentCash;
    }

    public int getCurrentCash() {
        return currentCash;
    }
}
