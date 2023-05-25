package cart.fixture;

import cart.domain.Member;
import cart.domain.OrderedProduct;

public class Fixture {
    public static final Member GOLD_MEMBER = new Member(1L, "a@a.com", "1234", 1);
    public static final Member SILVER_MEMBER = new Member(2L, "b@b.com", "1234", 2);
    public static final Member BRONZE_MEMBER = new Member(3L, "c@c.com", "1234", 3);
    public static final OrderedProduct ORDERED_PRODUCT1 = new OrderedProduct(
            1L,
            "치킨",
            10000,
            "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
            1);
}
