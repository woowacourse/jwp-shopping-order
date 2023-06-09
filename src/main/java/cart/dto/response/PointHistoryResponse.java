package cart.dto.response;

public class PointHistoryResponse {

    private final Long pointsSaved;

    public PointHistoryResponse(final Long pointsSaved) {
        this.pointsSaved = pointsSaved;
    }

    public Long getPointSaved() {
        return pointsSaved;
    }
}
