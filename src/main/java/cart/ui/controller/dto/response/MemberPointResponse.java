package cart.ui.controller.dto.response;

public class MemberPointResponse {

    private int point;

    private MemberPointResponse() {
    }

    public MemberPointResponse(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
