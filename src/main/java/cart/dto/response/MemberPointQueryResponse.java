package cart.dto.response;

public class MemberPointQueryResponse {
    private final int points;

    public MemberPointQueryResponse(final int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
