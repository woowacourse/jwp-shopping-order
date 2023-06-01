package cart.cart.application;

import cart.cart.Cart;
import cart.cart.domain.cartitem.application.CartItemRepository;
import cart.cart.domain.deliveryprice.DeliveryPrice;
import cart.coupon.application.CouponService;
import cart.member.Member;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class CartRepository {
    private final CartItemRepository cartItemRepository;

    public CartRepository(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public Cart findCart(Member member) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var totalPrice = cartItems.stream()
                .mapToInt(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();

        final var deliveryPrice = DeliveryPrice.calculateDeliveryPrice(totalPrice);
        return new Cart(cartItems, deliveryPrice);
    }
}
