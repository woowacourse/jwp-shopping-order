package coupon.application.dto;

public class CalculateActualPriceResponseDto {

    private final long actualPrice;

    public CalculateActualPriceResponseDto(long actualPrice) {
        this.actualPrice = actualPrice;
    }

    public long getActualPrice() {
        return actualPrice;
    }
}
