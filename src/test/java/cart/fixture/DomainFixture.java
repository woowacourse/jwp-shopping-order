package cart.fixture;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;

public class DomainFixture {

    public static final Member MEMBER_A = new Member(1L, "a@a.com", "1234");

    public static final Product CHICKEN = new Product(1L, "치킨" , 10_000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product SALAD = new Product(2L, "샐러드" , 20_000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product PIZZA = new Product(3L, "피자" , 13_000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

    public static final CartItem TWO_CHICKEN = new CartItem(1L, 2, MEMBER_A, CHICKEN);

}
