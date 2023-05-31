package cart.domain.order;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.product.Product;

import java.util.List;

public class OrderItem {

    private final Long id;
    private final Product product;
    private final int quantity;
    private final List<Coupon> coupons;
    private final Integer total;

    public OrderItem(final Long id, final Product product, final int quantity, final List<Coupon> coupons) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
        this.total = calculateDiscountedAmount(product, quantity, coupons);
    }

    public OrderItem(final Product product, final int quantity, final List<Coupon> coupons) {
        this(null, product, quantity, coupons);
    }

    private int calculateDiscountedAmount(final Product product, final int quantity, final List<Coupon> coupons) {
        int totalPrice = product.getPrice() * quantity;
        for (Coupon coupon : coupons) {
            Discount discount = coupon.getDiscount();
            System.out.println(discount.getDiscountType().calculate(totalPrice, discount.getAmount()));
            totalPrice -= discount.getDiscountType().calculate(totalPrice, discount.getAmount());
        }
        if (totalPrice > 0) {
            return totalPrice;
        }
        return 0;
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
