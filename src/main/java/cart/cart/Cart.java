package cart.cart;

import cart.cartitem.CartItem;
import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.discountcondition.DiscountTarget;

import java.util.List;

public class Cart {
    public static final int DEFAULT_DELIVERY_PRICE = 3_000;
    public static final int MAN_FREE_DELIVERY_PRICE = 30_000;

    private final List<CartItem> cartItems;
    private int originalDeliveryPrice;
    private int discountDeliveryPrice;
    private int discountFromTotalPrice;

    public Cart(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.originalDeliveryPrice = DEFAULT_DELIVERY_PRICE;
        this.discountFromTotalPrice = 0;
    }

    public int calculateTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getDiscountedPrice)
                .sum();
    }

    public void discount(DiscountPolicy discountPolicy) {
        if (discountPolicy.isTarget(DiscountTarget.SPECIFIC)) {
            for (CartItem cartItem : this.cartItems) {
                if (discountPolicy.isApplied(cartItem.getProductId())) {
                    cartItem.addDiscountPrice(discountPolicy.calculateDiscountPrice(cartItem.getOriginalPrice()));
                }
            }
            return;
        }

        if (discountPolicy.isTarget(DiscountTarget.ALL)) {
            for (CartItem cartItem : cartItems) {
                cartItem.addDiscountPrice(discountPolicy.calculateDiscountPrice(cartItem.getOriginalPrice()));
            }
            return;
        }

        if (discountPolicy.isTarget(DiscountTarget.DELIVERY)) {
            final var discountDeliveryPrice = discountPolicy.calculateDiscountPrice(originalDeliveryPrice);
            addDiscountForDeliveryPrice(discountDeliveryPrice);
            return;
        }

        if (discountPolicy.isTarget(DiscountTarget.TOTAL)) {
            final var discountPrice = discountPolicy.calculateDiscountPrice(calculateTotalPrice());
            addDiscountFromTotalPrice(discountPrice);
            return;
        }

        throw new IllegalArgumentException("할인정책을 진행할 수 없는 조건입니다.");
    }

    public int calculateFinalDeliveryPrice() {
        return this.originalDeliveryPrice - this.discountDeliveryPrice;
    }

    private void addDiscountForDeliveryPrice(int discountDeliveryPrice) {
        this.discountDeliveryPrice += discountDeliveryPrice;
    }

    private void addDiscountFromTotalPrice(int discountPrice) {
        this.discountFromTotalPrice += discountPrice;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getOriginalDeliveryPrice() {
        return originalDeliveryPrice;
    }

    public int getDiscountDeliveryPrice() {
        return discountDeliveryPrice;
    }

    public int getDiscountFromTotalPrice() {
        return discountFromTotalPrice;
    }
}
