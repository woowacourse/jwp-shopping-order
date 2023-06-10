package cart;

import cart.domain.Member;
import cart.domain.Product;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class ShoppingOrderFixture {
    public static final Member member1 = new Member(1L, "a@a.com", "password1", 10000L);
    public static final Member member2 = new Member(2L, "b@b.com", "5678", 100000L);

    public static final Product chicken = new Product(1L, "치킨", 10000, "chicken_image", 10.0, true);
    public static final Product salad = new Product(2L, "샐러드", 20000, "salad_image", 5.0, true);
    public static final Product pizza = new Product(3L, "피자", 13000, "pizza_url", 10.0, false);
}
