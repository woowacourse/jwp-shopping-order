package cart.dto;

import cart.domain.Point;

public class SavingPointResponse {

    private final int savingPoint;

    public SavingPointResponse(final Point savingPoint) {
        this(savingPoint.getValue());
    }

    public SavingPointResponse(final int savingPoint) {
        this.savingPoint = savingPoint;
    }

    public int getSavingPoint() {
        return savingPoint;
    }
}
