package cart.exception;

public class ProductNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "존재하지 않는 product 입니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
