package cart.fixture;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;

public class DomainFixture {

    public static final Member MEMBER_A = new Member(1L, "a@a.com", "1234");

    public static final Product CHICKEN = new Product(1L, "치킨" , 10_000, "https://image.com");
    public static final Product SALAD = new Product(2L, "샐러드" , 20_000, "https://image.com");
    public static final Product PIZZA = new Product(3L, "피자" , 13_000, "https://image.com");

    public static final CartItem TWO_CHICKEN = new CartItem(1L, 2, MEMBER_A, CHICKEN);

}
