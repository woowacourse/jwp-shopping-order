package cart.domain.order;

import cart.domain.Member;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;
    private final LocalDateTime orderTime;

    private Order(final Long id, final Member member, final OrderItems orderItems, final LocalDateTime orderTime) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.orderTime = orderTime;
    }

    public static Order beforePersisted(final Member member, final OrderItems orderItems,
        final LocalDateTime orderTime) {
        return new Order(null, member, orderItems, orderTime);
    }

    public static Order persisted(final Long id, final Member member, final OrderItems orderItems,
        final LocalDateTime created_at) {
        return new Order(id, member, orderItems, created_at);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems.getItems();
    }

    public Long getProductPrice() {
        return orderItems.getTotalPrice();
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }
}

//TODO : 카트가 주문이 되면 ORDER 를 반환한다.
//TODO : cart.order() -> Order order
//TODO : 주문 이벤트 카트에 있는 모든 상품을 삭제, 주문 상품으로 생성
