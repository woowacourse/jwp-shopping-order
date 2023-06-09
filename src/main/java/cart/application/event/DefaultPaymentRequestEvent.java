package cart.application.event;

import cart.domain.member.Member;
import cart.domain.product.Price;
import cart.dto.request.OrderRequest;

public class DefaultPaymentRequestEvent implements PaymentRequestEvent {

    private final Member member;
    private final Price price;
    private final OrderRequest orderRequest;

    public DefaultPaymentRequestEvent(
            final Member member,
            final Price price,
            final OrderRequest orderRequest

    ) {
        this.member = member;
        this.price = price;
        this.orderRequest = orderRequest;
    }

    public Member getMember() {
        return member;
    }

    public Price getPrice() {
        return price;
    }

    public OrderRequest getOrderRequest() {
        return orderRequest;
    }
}
