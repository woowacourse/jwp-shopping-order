package cart.domain;

import java.util.List;

public class OrderItem {

    private Long id;
    private Product product;
    private int quantity;
    private List<MemberCoupon> coupons;
    private Integer totalPrice;

    public OrderItem(Product product, int quantity, List<MemberCoupon> coupons, Integer totalPrice) {
        this(null, product, quantity, coupons, totalPrice);
    }

    public OrderItem(Long id, Product product, int quantity, List<MemberCoupon> coupons, Integer totalPrice) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
        this.totalPrice = totalPrice;
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

    public List<MemberCoupon> getCoupons() {
        return coupons;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
