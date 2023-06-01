package cart.domain.order;

import cart.domain.coupon.Discount;
import cart.domain.product.Product;
import cart.domain.member.MemberCoupon;

import java.util.List;

public class OrderItem {

    private final Long id;
    private final Product product;
    private final int quantity;
    private final List<MemberCoupon> coupons;
    private final Integer total;

    public OrderItem(final Long id, final Product product, final int quantity, final List<MemberCoupon> coupons) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
        this.total = calculateDiscountedAmount(product, quantity, coupons);
    }

    public OrderItem(final Product product, final int quantity, final List<MemberCoupon> coupons) {
        this(null, product, quantity, coupons);
    }

    private int calculateDiscountedAmount(final Product product, final int quantity, final List<MemberCoupon> coupons) {
        int totalPrice = product.getPrice() * quantity;
        for (MemberCoupon coupon : coupons) {
            Discount discount = coupon.getCoupon().getDiscount();
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

    public List<MemberCoupon> getCoupons() {
        return coupons;
    }

    public int getTotal() {
        return total;
    }
}
