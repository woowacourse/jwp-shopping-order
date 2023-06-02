package cart.exception;

public class MismatchedTotalPriceException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "저장된 상품들의 총 가격과 요청의 총 가격이 다릅니다.";

    public MismatchedTotalPriceException() {
        super(DEFAULT_MESSAGE);
    }
}
