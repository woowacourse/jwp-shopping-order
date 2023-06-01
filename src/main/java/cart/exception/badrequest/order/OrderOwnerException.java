package cart.exception.badrequest.order;

import cart.exception.badrequest.BadRequestException;

public class OrderOwnerException extends BadRequestException {

    public OrderOwnerException() {
        super("주문을 관리할 수 있는 멤버가 아닙니다.");
    }
}
