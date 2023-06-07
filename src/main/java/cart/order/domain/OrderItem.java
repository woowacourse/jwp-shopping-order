package cart.order.domain;

import static cart.order.exception.OrderExceptionType.INVALID_ORDER_ITEM_PRODUCT_QUANTITY;

import cart.cartitem.domain.CartItem;
import cart.coupon.domain.Coupon;
import cart.order.exception.OrderException;
import java.util.List;

public class OrderItem {

    private final Long id;
    private final int quantity;
    private final Long productId;
    private final String name;
    private final int productPrice;
    private final int orderedProductPrice;
    private final String imageUrl;

    public OrderItem(int quantity,
                     Long productId,
                     String name,
                     int productPrice,
                     int orderedProductPrice,
                     String imageUrl) {
        this(null, quantity, productId, name, productPrice, orderedProductPrice, imageUrl);
    }

    public OrderItem(Long id,
                     int quantity,
                     Long productId,
                     String name,
                     int productPrice,
                     int orderedProductPrice,
                     String imageUrl) {
        validateQuantity(quantity);
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.name = name;
        this.productPrice = productPrice;
        this.orderedProductPrice = orderedProductPrice;
        this.imageUrl = imageUrl;
    }

    public static OrderItem withCoupon(CartItem cartItem, List<Coupon> coupons) {
        return new OrderItem(cartItem.getQuantity(),
                cartItem.getProductId(),
                cartItem.getName(),
                cartItem.getPrice(),
                applyCoupon(cartItem, coupons),
                cartItem.getImageUrl());
    }

    private static int applyCoupon(CartItem cartItem, List<Coupon> coupons) {
        return coupons.stream()
                .filter(coupon -> coupon.canApply(cartItem.getProductId()))
                .findFirst()
                .map(coupon -> coupon.apply(cartItem.getPrice()))
                .orElseGet(cartItem::getPrice);
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new OrderException(INVALID_ORDER_ITEM_PRODUCT_QUANTITY);
        }
    }

    public int price() {
        return productPrice * quantity;
    }

    public int orderedPrice() {
        return orderedProductPrice * quantity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getOrderedProductPrice() {
        return orderedProductPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
