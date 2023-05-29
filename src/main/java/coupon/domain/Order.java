package coupon.domain;

public class Order {

    private final Money originalPrice;

    public Order(long originalPrice) {
        this.originalPrice = new Money(originalPrice);
    }

    public Money getOriginalPrice() {
        return originalPrice;
    }
}
