package cart.dto.response;

import cart.domain.Member;

public class PointResponse {

    private Integer points;

    public PointResponse(final Integer points) {
        this.points = points;
    }

    public static PointResponse of(Member member) {
        return new PointResponse(member.getPointAsInt());
    }

    public Integer getPoints() {
        return points;
    }
}
