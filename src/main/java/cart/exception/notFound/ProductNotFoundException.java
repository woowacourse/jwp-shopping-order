package cart.exception.notFound;

public class ProductNotFoundException extends NotFoundException {
    private static final String MESSAGE = "해당 product가 존재하지 않습니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
