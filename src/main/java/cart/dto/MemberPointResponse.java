package cart.dto;

public class MemberPointResponse {

    private final int point;

    public MemberPointResponse(final int point) {
        this.point = point;
    }

    public int getPoints() {
        return point;
    }
}
