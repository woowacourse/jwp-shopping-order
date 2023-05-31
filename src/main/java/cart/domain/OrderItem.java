package cart.domain;

import java.util.List;

public class OrderItem {

    private final Long id;
    private final Product product;
    private final int quantity;
    private final List<Coupon> coupons;
    private final int total;

    public OrderItem(final Long id, final Product product, final int quantity, final List<Coupon> coupons) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;

        int totalPrice = product.getPrice() * quantity;
        for (Coupon coupon : coupons) {
            Discount discount = coupon.getDiscount();
            System.out.println(discount.getDiscountType().calculate(totalPrice, discount.getAmount()));
            totalPrice -= discount.getDiscountType().calculate(totalPrice, discount.getAmount());
        }
        this.total = totalPrice;
    }

    public OrderItem(final Product product, final int quantity, final List<Coupon> coupons) {
        this.id = null;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
        int totalPrice = product.getPrice() * quantity;
        for (Coupon coupon : coupons) {
            Discount discount = coupon.getDiscount();
            totalPrice -= discount.getDiscountType().calculate(totalPrice, discount.getAmount());
        }
        this.total = totalPrice;
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

    public int getTotal() {
        return total;
    }
}
