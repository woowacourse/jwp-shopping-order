package cart.dto.response;

public class PointResponse {

    private long usablePoint;

    public PointResponse(final long usablePoint) {
        this.usablePoint = usablePoint;
    }

    public long getUsablePoint() {
        return usablePoint;
    }
}
