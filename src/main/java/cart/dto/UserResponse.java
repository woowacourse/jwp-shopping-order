package cart.dto;

public class UserResponse {

    private final String email;
    private final Long point;
    private final double earnRate;

    public UserResponse(final String email, final Long point, final double earnRate) {
        this.email = email;
        this.point = point;
        this.earnRate = earnRate;
    }

    public String getEmail() {
        return email;
    }

    public Long getPoint() {
        return point;
    }

    public double getEarnRate() {
        return earnRate;
    }
}
