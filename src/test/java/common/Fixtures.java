package common;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;

import java.util.List;

public class Fixtures {
    public static final Product 치킨 = new Product(1L, "치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product 샐러드 = new Product(2L, "샐러드", 20000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product 피자 = new Product(3L, "피자", 13000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

    public static final Member 멤버_A = new Member(1L, "a@a.com", "1234");
    public static final Member 멤버_B = new Member(2L, "b@b.com", "1234");

    public static final CartItem A_치킨_1 = new CartItem(1L, 1, 치킨, 멤버_A);
    public static final CartItem A_샐러드_1 = new CartItem(2L, 1, 샐러드, 멤버_A);
    public static final CartItem A_샐러드_2 = new CartItem(3L, 2, 샐러드, 멤버_A);
    public static final CartItem B_피자_1 = new CartItem(3L, 1, 피자, 멤버_B);

    public static final Order 주문_A_치킨_1 = Order.of(멤버_A, List.of(A_치킨_1));
    public static final Order 주문_A_치킨_1_샐러드_1 = Order.of(멤버_A, List.of(A_치킨_1, A_샐러드_1));
    public static final Order 주문_A_치킨_1_샐러드_2 = Order.of(멤버_A, List.of(A_치킨_1, A_샐러드_2));
}
