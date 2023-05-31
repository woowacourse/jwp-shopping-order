package cart.common.fixture;

import cart.domain.member.Member;
import cart.domain.product.Product;

public class DomainFixture {

    public static final String EMAIL = "huchu@woowahan.com";
    public static final String PASSWORD = "1234567a!";

    public static final String PRODUCT_NAME = "chicken";
    public static final String PRODUCT_IMAGE = "chicken.jpeg";

    public static Member HUCHU = new Member(1L, EMAIL, PASSWORD, 1000);
    public static Member HAMAD = new Member(1L, "hamad@woowahan.com", PASSWORD, 1000);

    public static Product CHICKEN = new Product(1L, "chicken", 20000, "chicken.jpeg");

}
