package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.product.Product;
import cart.dto.history.ProductHistory;
import cart.dto.product.ProductPriceAppliedAllDiscountResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cart {

    private final Long id;
    private final CartItems cartItems;
    private final DeliveryFee deliveryFee;

    public Cart(final Long id, final CartItems cartItems) {
        this.id = id;
        this.cartItems = cartItems;
        this.deliveryFee = DeliveryFee.createDefault();
    }

    public CartItem addItem(final Product product) {
        return cartItems.add(product);
    }

    public void removeItem(final CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public void changeQuantity(final long cartItemId, final int quantity) {
        cartItems.changeQuantity(cartItemId, quantity);
    }

    public boolean hasCartItem(final CartItem cartItem) {
        return cartItems.hasCartItem(cartItem);
    }

    public int calculateOriginPrice() {
        return cartItems.getTotalOriginPrice();
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

    public List<ProductHistory> buy(final List<Long> productIds, final List<Integer> quantities) {
        return IntStream.range(0, productIds.size())
                .mapToObj(index -> cartItems.buy(productIds.get(index), quantities.get(index)))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems.getCartItems();
    }

    public int getDeliveryFee() {
        return deliveryFee.getFee();
    }
}
