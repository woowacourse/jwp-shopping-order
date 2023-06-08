package cart.step2.order.exception;

import cart.step2.error.exception.ErrorCode;
import cart.step2.error.exception.ShoppingOrderException;

public class PriceIsNotValid extends ShoppingOrderException {

    public static final ShoppingOrderException THROW = new PriceIsNotValid();

    public PriceIsNotValid() {
        super(new ErrorCode(400, "클라이언트에서 받은 가격이 유효하지 않습니다. 알맞은 값인지 확인해주세요!"));
    }

}
