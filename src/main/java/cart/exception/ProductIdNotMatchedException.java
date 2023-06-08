package cart.exception;

import cart.step2.error.exception.ErrorCode;
import cart.step2.error.exception.ShoppingOrderException;

public class ProductIdNotMatchedException extends ShoppingOrderException {

    public static final ShoppingOrderException THROW = new ProductIdNotMatchedException();

    public ProductIdNotMatchedException() {
        super(new ErrorCode(500, "cart_item의 product_id(fk)와 일치하는 product의 id(pk)가 존재하지 않습니다."));
    }

}
