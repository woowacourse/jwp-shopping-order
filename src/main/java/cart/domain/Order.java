package cart.domain;

import cart.exception.PointException;
import cart.exception.PriceInconsistencyException;

import java.util.List;

public class Order {

    private Long id;
    private final Member member;
    private final List<CartItem> cartItems;
    private final Long originalPrice;
    private final Long usedPoint;
    private final Long pointToAdd;

    public Order(Member member, List<CartItem> cartItems, Long originalPrice, Long usedPoint, Long pointToAdd) {
        validateOriginalPrice(cartItems, originalPrice);
        validateUsedPoint(member, cartItems, usedPoint);
        validatePointToAdd(cartItems, pointToAdd);

        this.member = member;
        this.cartItems = cartItems;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public Order(Long id, Member member, List<CartItem> cartItems, Long originalPrice, Long usedPoint, Long pointToAdd) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    private void validateOriginalPrice(List<CartItem> cartItems, Long originalPrice) {
        int price = 0;
        for (CartItem item : cartItems) {
            price += item.getProduct().getPrice() * item.getQuantity();
        }

        if (originalPrice != price) {
            throw new PriceInconsistencyException("주문 총액에 문제가 있습니다");
        }
    }

    private void validateUsedPoint(Member member, List<CartItem> cartItems, Long usedPoint) {
        if (member.getPoint() < usedPoint) {
            throw new PointException.InvalidPointException("보유한 포인트보다 많은 포인트를 사용할 수 없습니다");
        }

        int price = 0;
        for (CartItem item : cartItems) {
            if (item.getProduct().getPointAvailable()) {
                price += item.getProduct().getPrice();
            }
        }

        if (price < usedPoint) {
            throw new PointException.InvalidPointException("사용 가능한 포인트보다 많은 포인트를 사용할 수 없습니다");
        }
    }

    private void validatePointToAdd(List<CartItem> cartItems, Long pointToAdd) {
        double point = 0.0;
        for (CartItem item : cartItems) {
             point += item.getProduct().getPrice() * item.getQuantity() * item.getProduct().getPointRatio() / 100;
        }

        if (pointToAdd != Math.round(point)) {
            throw new PointException.PointInconsistencyException("적립될 포인트에 문제가 있습니다");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public Long getPointToAdd() {
        return pointToAdd;
    }
}
