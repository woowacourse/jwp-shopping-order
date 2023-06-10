package cart.cart_item.exception;

import cart.cart_item.domain.CartItem;
import cart.member.domain.Member;

public class CartItemException extends RuntimeException {

  public CartItemException(String message) {
    super(message);
  }

  public static class IllegalMember extends CartItemException {

    public IllegalMember(CartItem cartItem, Member member) {
      super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId="
          + member.getId());
    }
  }
}