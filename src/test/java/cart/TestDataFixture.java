package cart;

import cart.domain.Member;
import cart.domain.Product;

public class TestDataFixture {

    //Product
    public static final Product PRODUCT_1 = new Product(1L, "치킨", 10000,
            "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product PRODUCT_2 = new Product(2L,"샐러드", 20000,
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static final Product PRODUCT_3 = new Product(3L, "피자", 13000,
            "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

    //Member
    public static final Member MEMBER_1 = new Member(1L, "a@a.com", "1234");
    public static final Member MEMBER_2 = new Member(2L, "b@b.com", "1234");
    public static final Member MEMBER_3 = new Member(3L, "sangun", "1234");
    public static final Member MEMBER_4 = new Member(3L, "lopi", "1234");
}
