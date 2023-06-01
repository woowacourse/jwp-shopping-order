package cart.domain.order;

import cart.domain.member.dto.MemberWithId;

public class OrderWithId {

    private final Long orderId;
    private final Order order;

    public OrderWithId(final Long orderId, final Order order) {
        this.orderId = orderId;
        this.order = order;
    }

    public boolean isOwner(final String memberName) {
        final MemberWithId memberWithId = order.getMember();
        return memberWithId.getMember().name().equals(memberName);
    }

    public Long getOrderId() {
        return orderId;
    }

    public Order getOrder() {
        return order;
    }
}
