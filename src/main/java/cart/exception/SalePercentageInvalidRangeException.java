package cart.exception;

public class SalePercentageInvalidRangeException extends RuntimeException {

    public SalePercentageInvalidRangeException(final int percentage) {
        super("상품 세일은 1~100% 까지 가능합니다. / 입력하신 값 : " + percentage);
    }
}
