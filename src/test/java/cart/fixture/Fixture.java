package cart.fixture;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;

public class Fixture {
    public static final String EMAIL = "a@a.com";
    public static final String PASSWORD = "1234";
    public static final Member TEST_MEMBER = new Member(1L, EMAIL, PASSWORD);
    public static final Product PRODUCT = new Product(1L, "제네시스 g80", 1000, "https://www.genesis.com/content/dam/genesis-p2/kr/assets/utility/sns/genesis-kr-model-g80-02-social-1200x630-ko.jpg");
    public static final CartItem CART_ITEM = new CartItem(TEST_MEMBER, PRODUCT);

}
