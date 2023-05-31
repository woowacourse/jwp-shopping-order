package cart.domain;

import java.util.List;

public class OrderItem {

    private Long id;
    private Product product;
    private int quantity;
    private List<Coupon> coupons;

    public OrderItem(Product product, int quantity, List<Coupon> coupons) {
        this(null, product, quantity, coupons);
    }

    public OrderItem(Long id, Product product, int quantity, List<Coupon> coupons) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }
}
