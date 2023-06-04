package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "멤버별 포인트 조회 응답")
public class MemberPointResponse {

    @Schema(description = "멤버가 가진 포인트", example = "4000")
    private final int point;

    public MemberPointResponse(final int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
