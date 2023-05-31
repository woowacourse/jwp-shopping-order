package cart.dto;

import cart.domain.Point;

public class SavingPointResponse {
    private Long savingPoint;

    public SavingPointResponse() {
    }

    public SavingPointResponse(Long savingPoint) {
        this.savingPoint = savingPoint;
    }

    public static SavingPointResponse of(Point point) {
        return new SavingPointResponse(point.getValue());
    }

    public Long getSavingPoint() {
        return savingPoint;
    }
}
