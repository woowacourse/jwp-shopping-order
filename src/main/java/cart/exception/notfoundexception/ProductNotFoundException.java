package cart.exception.notfoundexception;

public class ProductNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 상품입니다. productId = %d";

    public ProductNotFoundException(long productId) {
        super(String.format(MESSAGE, productId));
    }

}
