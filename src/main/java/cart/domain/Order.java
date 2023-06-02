package cart.domain;

import cart.exception.InvalidPointException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Order {
    private final Long id;
    private final int usedPoint;
    private final Map<Product, Integer> orderedProducts;
    private final Member member;

    public Order(
            final Long id,
            final int usedPoint,
            final Map<Product, Integer> orderedProducts,
            final Member member
    ) {
        this.id = id;
        this.usedPoint = usedPoint;
        this.orderedProducts = new HashMap<>(orderedProducts);
        this.member = member;
    }

    public Order(
            final int usedPoint,
            final Map<Product, Integer> orderedProducts,
            final Member member
    ) {
        this(null, usedPoint, new HashMap<>(orderedProducts), member);
    }

    public static Order from(
            final int usedPoint,
            final Map<Product, Integer> orderedProducts,
            final Member member
    ) {
        return new Order(null, usedPoint, orderedProducts, member);
    }

    private static void validatePointAmount(final int savedPoint, final int usedPoint) {
        if (savedPoint < usedPoint) {
            throw new InvalidPointException("존재하는 포인트보다 더 많은 포인트를 사용할 수 없습니다.");
        }
    }

    public int calculatedUpdatedPoint(final int savedPoint) {
        validatePointAmount(savedPoint, usedPoint);
        return savedPoint - usedPoint;
    }

    public int calculateTotalPrice() {
        return calculateOriginalPrice() - usedPoint;
    }

    public int calculateOriginalPrice() {
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

    public Long getId() {
        return id;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public Map<Product, Integer> getOrderedProducts() {
        return orderedProducts;
    }

    public Member getMember() {
        return member;
    }

    public int countTotalAmount() {
        return orderedProducts.keySet().size();
    }

    public List<String> getProductNames() {
        return orderedProducts.keySet()
                .stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", usedPoint=" + usedPoint +
                ", orderedProducts=" + orderedProducts +
                ", member=" + member +
                '}';
    }
}
