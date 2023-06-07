package cart.dto;

import cart.domain.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "적립되는 포인트 조회 응답")
public class SavingPointResponse {

    @Schema(description = "예상 적립 포인트", example = "400")
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
