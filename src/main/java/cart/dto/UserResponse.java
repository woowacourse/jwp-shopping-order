package cart.dto;

public class UserResponse {

    private final String email;
    private final int point;
    private final double earnRate;

    public UserResponse(final String email, final int point, final double earnRate) {
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
