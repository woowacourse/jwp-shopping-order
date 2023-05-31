package cart.dto;

public final class MemberForOrderResponse {

    private String email;
    private int point;
    private double earnRate;

    public MemberForOrderResponse(String email, int point, double earnRate) {
        this.email = email;
        this.point = point;
        this.earnRate = earnRate;
    }

    public String getEmail() {
        return email;
    }

    public int getPoint() {
        return point;
    }

    public double getEarnRate() {
        return earnRate;
    }
}
