package cart.dto;

public class MemberPointResponse {

    private final int points;

    public MemberPointResponse(final int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
