package cart.domain;

import cart.exception.InvalidPointException;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private final int savedPoint;
    private final int usedPoint;
    private final Map<Product, Integer> orderedProducts;
    private final Member member;

    public Order(
            final int savedPoint,
            final int usedPoint,
            final Map<Product, Integer> orderedProducts,
            final Member member
    ) {
        this.savedPoint = savedPoint;
        this.usedPoint = usedPoint;
        this.orderedProducts = new HashMap<>(orderedProducts);
        this.member = member;
    }

    public static Order from(
            final int savedPoint,
            final int usedPoint,
            final Map<Product, Integer> orderedProducts,
            final Member member
    ) {
        validatePointAmount(savedPoint, usedPoint);
        return new Order(savedPoint, usedPoint, orderedProducts, member);
    }

    private static void validatePointAmount(final int savedPoint, final int usedPoint) {
        if (savedPoint < usedPoint) {
            throw new InvalidPointException("존재하는 포인트보다 더 많은 포인트를 사용할 수 없습니다.");
        }
    }

    public int calculatedUpdatedPoint() {
        return savedPoint - usedPoint;
    }

    public int calculateOrderPrice() {
        return calculateTotalPrice() - usedPoint;
    }

    public int calculateTotalPrice() {
        return orderedProducts.keySet().stream()
                .mapToInt(
                        product -> {
                            final int price = product.getPrice();
                            final int quantity = orderedProducts.get(product);
                            return price * quantity;
                        }
                )
                .sum();
    }
}
