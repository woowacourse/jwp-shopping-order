package cart;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;

public class TestFixture {
    public static final Product PRODUCT1 = new Product(1L, "치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product PRODUCT2 = new Product(2L, "샐러드", 20000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product PRODUCT3 = new Product(3L, "피자", 13000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

    public static Member getMember1() {
        return new Member(1L, "a@a.com", "1234", 0);
    }

    public static Member getMember2() {
        return new Member(2L, "b@b.com", "1234", 0);
    }

    public static CartItem getCartItem1() {
        return new CartItem(1L, getMember1(), PRODUCT1, 2);
    }

    public static CartItem getCartItem2() {
        return new CartItem(2L, getMember1(), PRODUCT2, 4);
    }

    public static CartItem getCartItem3() {
        return new CartItem(3L, getMember2(), PRODUCT3, 5);
    }
}
