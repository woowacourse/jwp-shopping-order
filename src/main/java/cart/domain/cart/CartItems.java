package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.dto.history.OrderedProductHistory;
import cart.dto.product.ProductPriceAppliedAllDiscountResponse;
import cart.exception.CartItemNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void validateBuyingProduct(final List<Long> productIds, final List<Integer> quantities) {
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);

            CartItem cartItem = getCartItemByProductId(productId);

            cartItem.validateQuantity(quantities.get(i));
        }
    }

    public List<ProductPriceAppliedAllDiscountResponse> getProductPricesAppliedAllDiscount(final List<Coupon> usingCoupons) {
        List<Integer> originPrices = getOriginPrices();
        List<Integer> afterPrices = calculateAfterPrices(originPrices, usingCoupons);

        return generateProductResultPrices(originPrices, afterPrices);
    }

    private List<Integer> getOriginPrices() {
        return cartItems.stream()
                .map(CartItem::getAppliedDiscountOrOriginPrice)
                .collect(Collectors.toList());
    }

    private List<Integer> calculateAfterPrices(final List<Integer> originPrices, final List<Coupon> usingCoupons) {
        List<Coupon> usableCoupons = getUsableCoupons(usingCoupons);

        return originPrices.stream()
                .map(originPrice -> calculateAppliedCouponsPrice(originPrice, usableCoupons))
                .collect(Collectors.toList());
    }

    private List<Coupon> getUsableCoupons(final List<Coupon> usingCoupons) {
        return usingCoupons.stream()
                .filter(coupon -> !coupon.isDeliveryCoupon())
                .collect(Collectors.toList());
    }

    private int calculateAppliedCouponsPrice(final int originPrice, final List<Coupon> usableCoupons) {
        int price = originPrice;

        for (final Coupon coupon : usableCoupons) {
            price = coupon.calculate(price);
        }

        return price;
    }

    private List<ProductPriceAppliedAllDiscountResponse> generateProductResultPrices(final List<Integer> originPrices, final List<Integer> afterPrices) {
        List<ProductPriceAppliedAllDiscountResponse> result = new ArrayList<>();

        for (int i = 0; i < cartItems.size(); i++) {
            int originPrice = originPrices.get(i);
            int afterPrice = afterPrices.get(i);
            int discount = originPrice - afterPrice;

            result.add(new ProductPriceAppliedAllDiscountResponse(cartItems.get(i).getId(), originPrice, discount));
        }

        return result;
    }

    public OrderedProductHistory buy(final Long productId, final int quantity) {
        CartItem cartItem = getCartItemByProductId(productId);
        int totalPrice = cartItem.getAppliedDiscountOrOriginPrice(quantity);

        cartItem.buy(quantity);

        return new OrderedProductHistory(productId, cartItem.getProduct().getName(), cartItem.getProduct().getImageUrl(), quantity, totalPrice);
    }

    private CartItem getCartItemByProductId(final Long productId) {
        return cartItems.stream()
                .filter(item -> item.hasProduct(productId))
                .findAny()
                .orElseThrow(() -> new CartItemNotFoundException(productId));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
