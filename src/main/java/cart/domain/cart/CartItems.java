package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.order.ProductHistory;
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

    public void validateBuyingProduct(final List<Long> productIds, final List<Integer> quantities) {
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);

            CartItem cartItem = cartItems.stream()
                    .filter(it -> it.hasProduct(productId))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("요청하신 상품을 찾을 수 없습니다."));

            cartItem.validateQuantity(quantities.get(i));
        }
    }

    public boolean hasItem(final CartItem cartItem) {
        return cartItems.stream()
                .anyMatch(item -> item.hasSameId(cartItem.getId()));
    }

    public int getTotalOriginPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getOriginPrice)
                .sum();
    }

    public int getTotalFinallyPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::calculatePriceWithDiscount)
                .sum();
    }

    public List<ProductUsingCouponAndSaleResponse> getProductUsingCouponAndSaleResponse(final List<Coupon> reqCoupon) {
        List<ProductUsingCouponAndSaleResponse> result = new ArrayList<>();

        List<Integer> originPrices = cartItems.stream()
                .map(CartItem::calculatePriceWithDiscount)
                .collect(Collectors.toList());

        List<Integer> afterPrices = new ArrayList<>();
        for (Integer originPrice : originPrices) {
            int afterPrice = originPrice;

            for (Coupon coupon : reqCoupon) {
                if (!coupon.isDeliveryCoupon()) {
                    afterPrice = coupon.calculate(afterPrice);
                }
            }
            afterPrices.add(afterPrice);
        }

        for (int i = 0; i < cartItems.size(); i++) {
            result.add(new ProductUsingCouponAndSaleResponse(cartItems.get(i).getId(), originPrices.get(i), originPrices.get(i) - afterPrices.get(i)));
        }

        return result;
    }

    public ProductHistory buy(final Long productId, final int quantity) {
        // 1. CartItem 을 찾아온다.
        CartItem cartItem = cartItems.stream()
                .filter(item -> item.hasProduct(productId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요청하신 상품을 찾을 수 없습니다."));

        // 2. 최종 가격 * quantity 가격을 가져온다.
        int totalPrice = cartItem.calculatePriceWithDiscount(quantity);

        // 3. 도메인에서 상품의 수량을 낮춘다.
        cartItem.buy(quantity);

        return new ProductHistory(productId, cartItem.getProduct().getName(), quantity, totalPrice);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
