package cart.application.event;

import cart.domain.member.Member;
import cart.domain.product.Price;
import cart.dto.request.OrderRequest;

public interface PaymentRequestEvent {

    Member getMember();

    Price getPrice();

    OrderRequest getOrderRequest();
}
