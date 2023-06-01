package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.product.Product;
import cart.dto.history.OrderedProductHistory;
import cart.dto.product.ProductPriceAppliedAllDiscountResponse;
import cart.exception.CartItemNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartItems {

    private static final int EMPTY_COUNT = 0;

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public CartItem add(final Product product) {
        Optional<CartItem> cartItem = cartItems.stream()
                .filter(item -> item.hasProduct(product))
                .findAny();

        if (cartItem.isPresent()) {
            CartItem insertItem = cartItem.get();
            insertItem.addQuantity();
            return insertItem;
        }

        CartItem insertItem = new CartItem(product);
        cartItems.add(insertItem);
        return insertItem;
    }

    public void remove(final CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public void changeQuantity(final long cartItemId, final int quantity) {
        CartItem cartItem = findCartItemByCartItemId(cartItemId);

        if (quantity == EMPTY_COUNT) {
            this.remove(cartItem);
            return;
        }

        cartItem.changeQuantity(quantity);
    }

    private CartItem findCartItemByCartItemId(final long cartItemId) {
        return cartItems.stream()
                .filter(item -> item.isSame(cartItemId))
                .findAny()
                .orElseThrow(CartItemNotFoundException::new);
    }

    public void validateBuyingProduct(final List<Long> productIds, final List<Integer> quantities) {
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);

            CartItem cartItem = getCartItemByProductId(productId);

            cartItem.validateQuantity(quantities.get(i));
        }
    }

    public boolean hasCartItem(final CartItem cartItem) {
        return cartItems.stream()
                .anyMatch(item -> item.isSame(cartItem.getId()));
    }

    public int getTotalOriginPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getOriginPrice)
                .sum();
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
                .orElseThrow(CartItemNotFoundException::new);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
