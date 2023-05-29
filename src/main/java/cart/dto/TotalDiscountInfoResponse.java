package cart.dto;

public class TotalDiscountInfoResponse {
    private final DiscountInfoResponse gradeDiscount;
    private final DiscountInfoResponse priceDiscount;

    public TotalDiscountInfoResponse(
            DiscountInfoResponse gradeDiscount,
            DiscountInfoResponse priceDiscount
    ) {
        this.gradeDiscount = gradeDiscount;
        this.priceDiscount = priceDiscount;
    }

    public DiscountInfoResponse getGradeDiscount() {
        return gradeDiscount;
    }

    public DiscountInfoResponse getPriceDiscount() {
        return priceDiscount;
    }
}
