package cart.domain;

import cart.domain.vo.Amount;

public class Order {

    private final Long id;
    private final Products products;
    private final Amount totalAmount;
    private final Amount discountedAmount;
    private final Amount deliveryAmount;
    private final String address;

    public Order(final Products products, final Amount totalAmount, final Amount discountedAmount,
        final Amount deliveryAmount, final String address) {
        this(null, products, totalAmount, discountedAmount, deliveryAmount, address);
    }

    public Order(final Long id, final Products products, final Amount totalAmount,
        final Amount discountedAmount, final Amount deliveryAmount, final String address) {
        this.id = id;
        this.products = products;
        this.totalAmount = totalAmount;
        this.discountedAmount = discountedAmount;
        this.deliveryAmount = deliveryAmount;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public Products getProducts() {
        return products;
    }

    public Amount getDeliveryAmount() {
        return deliveryAmount;
    }

    public String getAddress() {
        return address;
    }

    public Amount getTotalAmount() {
        return totalAmount;
    }

    public Amount getDiscountedAmount() {
        return discountedAmount;
    }
}
