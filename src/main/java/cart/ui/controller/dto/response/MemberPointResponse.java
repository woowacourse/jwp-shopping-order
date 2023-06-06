package cart.ui.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "멤버 포인트 응답")
public class MemberPointResponse {

    @Schema(description = "멤버 포인트", example = "3000")
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
