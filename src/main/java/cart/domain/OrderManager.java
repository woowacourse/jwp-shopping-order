package cart.domain;

import cart.exception.PointException;
import cart.exception.PriceInconsistencyException;

import java.util.List;

public class OrderManager {

    private final List<CartItem> cartItems;

    public OrderManager(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void validateOrder(Order order) {
        validateOriginalPrice(order.getOriginalPrice());
        validateUsedPoint(order.getMember(), order.getUsedPoint());
        validatePointToAdd(order.getPointToAdd());
    }

    private void validateOriginalPrice(Long originalPrice) {
        int price = 0;
        for (CartItem item : cartItems) {
            price += item.getProduct().getPrice() * item.getQuantity();
        }

        if (originalPrice != price) {
            throw new PriceInconsistencyException("주문 총액에 문제가 있습니다");
        }
    }

    private void validateUsedPoint(Member member, Long usedPoint) {
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

    private void validatePointToAdd(Long pointToAdd) {
        double point = 0.0;
        for (CartItem item : cartItems) {
            point += item.getProduct().getPrice() * item.getQuantity() * item.getProduct().getPointRatio() / 100;
        }

        if (pointToAdd != Math.round(point)) {
            throw new PointException.PointInconsistencyException("적립될 포인트에 문제가 있습니다");
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
