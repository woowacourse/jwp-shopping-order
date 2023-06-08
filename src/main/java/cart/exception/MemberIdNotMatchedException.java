package cart.exception;

import cart.step2.error.exception.ErrorCode;
import cart.step2.error.exception.ShoppingOrderException;

public class MemberIdNotMatchedException extends ShoppingOrderException {

    public static final ShoppingOrderException THROW = new MemberIdNotMatchedException();

    public MemberIdNotMatchedException() {
        super(new ErrorCode(500, "cart_item의 member_id(fk)와 일치하는 member의 id(pk)가 존재하지 않습니다."));
    }

}
