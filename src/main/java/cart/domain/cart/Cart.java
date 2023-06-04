package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.dto.history.OrderedProductHistory;
import cart.dto.product.ProductPriceAppliedAllDiscountResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cart {

    private final Long id;
    private Long memberId;
    private final CartItems cartItems;
    private final DeliveryFee deliveryFee;

    public Cart(final Long id, final CartItems cartItems) {
        this.id = id;
        this.cartItems = cartItems;
        this.deliveryFee = DeliveryFee.createDefault();
    }

    public Cart(final Long id, final Long memberId, final CartItems cartItems) {
        this.id = id;
        this.cartItems = cartItems;
        this.memberId = memberId;
        this.deliveryFee = DeliveryFee.createDefault();
    }

    public int calculateDeliveryFeeUsingCoupons(final List<Coupon> usingCoupons) {
        int price = deliveryFee.getFee();

        for (final Coupon coupon : usingCoupons) {
            price = useDeliveryCoupon(price, coupon);
        }

        return price;
    }

    private int useDeliveryCoupon(int price, final Coupon coupon) {
        if (coupon.isDeliveryCoupon()) {
            price = coupon.calculate(price);
        }

        return price;
    }

    public List<ProductPriceAppliedAllDiscountResponse> getProductUsingCouponAndSaleResponses(final List<Coupon> usingCoupons) {
        return cartItems.getProductPricesAppliedAllDiscount(usingCoupons);
    }

    public void validateBuying(final List<Long> productIds, final List<Integer> quantities) {
        cartItems.validateBuyingProduct(productIds, quantities);
    }

    public List<OrderedProductHistory> buy(final List<Long> productIds, final List<Integer> quantities) {
        return IntStream.range(0, productIds.size())
                .mapToObj(index -> cartItems.buy(productIds.get(index), quantities.get(index)))
                .collect(Collectors.toList());
    }

    public boolean isOwner(final Long id) {
        return Objects.equals(this.memberId, id);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<CartItem> getCartItems() {
        return cartItems.getCartItems();
    }

    public int getDeliveryFee() {
        return deliveryFee.getFee();
    }
}
