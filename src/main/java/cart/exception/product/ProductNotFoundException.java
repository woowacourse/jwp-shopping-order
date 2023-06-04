package cart.exception.product;

public class ProductNotFoundException extends ProductException{

    private static final String PRODUCT_NOT_FOUND_EXCEPTION = "존재하지 않는 상품입니다.";

    public ProductNotFoundException() {
        super(PRODUCT_NOT_FOUND_EXCEPTION);
    }
}
