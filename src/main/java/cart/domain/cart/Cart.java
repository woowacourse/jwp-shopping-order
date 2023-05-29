package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.order.ProductHistory;
import cart.domain.product.Product;
import cart.dto.product.ProductUsingCouponAndSaleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean hasItem(final CartItem cartItem) {
        return cartItems.hasItem(cartItem);
    }


    public int calculateOriginPrice() {
        // 원래 상품 할인 전 가격 반환
        return cartItems.getTotalOriginPrice();
    }

    public int calculateDeliveryFeeUsingCoupons(final List<Coupon> reqCoupons) {
        int price = deliveryFee.getFee();

        List<Coupon> coupons = reqCoupons.stream()
                .filter(Coupon::isDeliveryCoupon)
                .collect(Collectors.toList());

        for (Coupon coupon : coupons) {
            price = coupon.calculate(price);
        }

        return price;
    }

    public List<ProductUsingCouponAndSaleResponse> getProductUsingCouponAndSaleResponse(final List<Coupon> requestCoupons) {
        return cartItems.getProductUsingCouponAndSaleResponse(requestCoupons);
    }

    public void validateBuying(final List<Long> productIds, final List<Integer> quantities) {
        cartItems.validateBuyingProduct(productIds, quantities);
    }

    public List<ProductHistory> buy(final List<Long> productIds, final List<Integer> quantities) {
        List<ProductHistory> productHistories = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            int quantity = quantities.get(i);

            ProductHistory productHistory = cartItems.buy(productId, quantity);
            productHistories.add(productHistory);
        }

        return productHistories;
    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems.getCartItems();
    }

    public List<String> getProductNames() {
        return cartItems.getCartItems().stream()
                .map(cartItem -> cartItem.getProduct().getName())
                .collect(Collectors.toList());
    }

    public int getDeliveryFee() {
        return deliveryFee.getFee();
    }
}
