package cart.exception;

public class SalePercentageInvalidRangeException extends RuntimeException {

    public SalePercentageInvalidRangeException() {
        super("상품 세일은 1~100% 까지 가능합니다.");
    }
}
