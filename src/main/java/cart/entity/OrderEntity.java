package cart.entity;

import cart.domain.Member;
import cart.exception.OrderAuthorizationException;

public class OrderEntity {

    private final long id;
    private final long memberId;
    private final int price;

    public OrderEntity(final long id, final long memberId, final int price) {
        this.id = id;
        this.memberId = memberId;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getPrice() {
        return price;
    }

    public void checkOwner(final Member member) {
        if (memberId != member.getId()) {
            throw new OrderAuthorizationException("주문은 소유자만 접근할 수 있습니다");
        }
    }
}
