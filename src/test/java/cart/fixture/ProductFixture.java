package cart.fixture;

import cart.domain.Product;

public class ProductFixture {

    public static final Product CHICKEN = new Product(1L, "치킨", 10_000, "www.naver.com");
    public static final Product PIZZA = new Product(2L, "피자", 20_000, "www.kakao.com");

    public static final Product CHICKEN_NO_ID = new Product("치킨", 10_000, "www.naver.com");
    public static final Product PIZZA_NO_ID = new Product("피자", 15_000, "www.kakao.com");


}
