package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.vo.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CartItems {

    private final List<CartItem> cartItems;

    private CartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public static CartItems empty() {
        return new CartItems(new ArrayList<>());
    }

    public static CartItems from(List<CartItem> cartItems) {
        return new CartItems(cartItems);
    }

    public void add(CartItem other) {
        Optional<CartItem> maybeCartItem = findSameCartItem(other);

        if (maybeCartItem.isPresent()) {
            CartItem newCartItem = other.mergePlus(maybeCartItem.get());

            cartItems.remove(maybeCartItem.get());
            cartItems.add(newCartItem);
            return;
        }
        cartItems.add(other);
    }

    private Optional<CartItem> findSameCartItem(CartItem other) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.isSameProduct(other))
                .findFirst();
    }

    public void remove(CartItem other) {
        Optional<CartItem> maybeCartItem = findSameCartItem(other);

        if (maybeCartItem.isPresent()) {
            CartItem newCartItem = other.mergeMinus(maybeCartItem.get());

            cartItems.remove(maybeCartItem.get());
            addCartItemWhenQuantityIsNotZero(newCartItem);
        }
    }

    private void addCartItemWhenQuantityIsNotZero(CartItem cartItem) {
        if (!cartItem.isQuantityZero()) {
            cartItems.add(cartItem);
        }
    }

    public void checkOwner(Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }

    public Money totalPrice() {
        Money money = Money.from(0);
        for (CartItem cartItem : cartItems) {
            money = money.plus(cartItem.totalPrice());
        }

        return money;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItems cartItems1 = (CartItems) o;
        return Objects.equals(cartItems, cartItems1.cartItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItems);
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "cartItems=" + cartItems +
                '}';
    }
}
