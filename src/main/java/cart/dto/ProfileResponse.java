package cart.dto;

public class ProfileResponse {

    private int points;

    public ProfileResponse() {
    }

    public ProfileResponse(final int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
