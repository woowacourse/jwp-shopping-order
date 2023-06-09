package cart.application.event;

import cart.domain.member.Member;
import cart.dto.request.OrderRequest;

public interface MemberPointUpdateEvent {

    Member getMember();

    OrderRequest getOrderRequest();

    Long getOrderId();
}
