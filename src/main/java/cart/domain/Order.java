package cart.domain;

import java.util.List;

public class Order {

    private final Member member;
    private final Integer usedPoint;
    private final OrderProducts products;

    public Order(final Member member, final Integer usedPoint, final List<CartItem> cartItems) {
        validatePoint(member, usedPoint);
        this.member = member;
        this.usedPoint = usedPoint;
        this.products = new OrderProducts(cartItems);
    }

    private static void validatePoint(final Member member, final Integer usedPoint) {
        if (!member.isAbleToUse(usedPoint)) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
    }

    public Member getMember() {
        return member;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public OrderProducts getProducts() {
        return products;
    }

    public Integer getTotalAmount() {
        return products.getTotalAmount();
    }

    public Integer getSavedPoint() {
        return products.getSavedPoint();
    }
}
