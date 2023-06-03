package cart.application.exception;

public class ExceedingProductsPointException extends RuntimeException {

    private static final String MESSAGE = "해당 상품들에 대해 사용 가능한 적립금을 초과했습니다.";

    public ExceedingProductsPointException() {
        super(MESSAGE);
    }
}
