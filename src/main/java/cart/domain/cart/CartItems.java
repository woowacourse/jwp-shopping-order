package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.product.Product;
import cart.dto.product.ProductUsingCouponAndSaleResponse;
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
            CartItem insertItem = cartItem.orElseThrow(IllegalArgumentException::new);
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
        CartItem cartItem = cartItems.stream()
                .filter(item -> item.hasSameId(cartItemId))
                .findAny()
                .orElseThrow(CartItemNotFoundException::new);

        if (quantity == EMPTY_COUNT) {
            this.remove(cartItem);
            return;
        }

        cartItem.changeQuantity(quantity);
    }

    public boolean hasItem(final CartItem cartItem) {
        return cartItems.stream()
                .anyMatch(item -> item.hasSameId(cartItem.getId()));
    }

    public int getTotalPriceWithoutCoupons() {
        return cartItems.stream()
                .mapToInt(CartItem::getOriginPrice)
                .sum();
    }

    public int getTotalPriceUsingCoupons() {
        return cartItems.stream()
                .mapToInt(CartItem::getFinallyPriceWithOutCoupon)
                .sum();
    }

    public List<ProductUsingCouponAndSaleResponse> getProductUsingCouponAndSaleResponse(final List<Coupon> reqCoupon) {
        List<ProductUsingCouponAndSaleResponse> result = new ArrayList<>();

        List<Integer> originPrices = cartItems.stream()
                .map(CartItem::getOriginPrice)
                .collect(Collectors.toList());

        List<Integer> afterPrices = new ArrayList<>();
        for (Integer originPrice : originPrices) {
            int afterPrice = originPrice;
            for (Coupon coupon : reqCoupon) {
                if (coupon.isDeliveryCoupon()) {
                    continue;
                }
                afterPrice = coupon.calculate(afterPrice);
            }
            afterPrices.add(afterPrice);
        }

        for (int i = 0; i < cartItems.size(); i++) {
            result.add(new ProductUsingCouponAndSaleResponse(cartItems.get(i).getId(), originPrices.get(i), afterPrices.get(i)));
        }

        return result;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
