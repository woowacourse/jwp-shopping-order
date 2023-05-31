package cart.dto;

public class PointResponse {
    private Long memberId;
    private Long totalPoint;

    private PointResponse() {
    }

    public PointResponse(Long memberId, Long totalPoint) {
        this.memberId = memberId;
        this.totalPoint = totalPoint;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getTotalPoint() {
        return totalPoint;
    }
}
