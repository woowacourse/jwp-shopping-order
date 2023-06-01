package cart.member.dto;

public class PointResponse {
    private final Long point;
    
    public PointResponse(final Long point) {
        this.point = point;
    }
    
    public Long getPoint() {
        return point;
    }
}
