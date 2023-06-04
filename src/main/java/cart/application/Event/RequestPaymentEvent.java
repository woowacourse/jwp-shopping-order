package cart.application.Event;

import cart.domain.member.Member;
import cart.domain.product.Price;
import cart.dto.request.OrderRequest;

public class RequestPaymentEvent {

    private final Member member;
    private final Price price;
    private final OrderRequest orderRequest;

    public RequestPaymentEvent(
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
