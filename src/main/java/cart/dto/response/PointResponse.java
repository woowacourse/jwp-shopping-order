package cart.dto.response;

import cart.domain.Member;

public class PointResponse {

    private Integer point;

    public PointResponse(final Integer point) {
        this.point = point;
    }

    public static PointResponse of(Member member) {
        return new PointResponse(member.getPoint().getValue());
    }

    public Integer getPoint() {
        return point;
    }
}
