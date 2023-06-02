package cart.acceptence.fixtures;

import cart.dto.request.ProductRequest;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixtures {

    public static final ProductRequest 치킨_10000원 = new ProductRequest("치킨", 10_000L, "http://example.com/chicken.jpg");
    public static final ProductRequest 피자_15000원 = new ProductRequest("피자", 15_000L, "http://example.com/pizza.jpg");
    public static final ProductRequest 피자_18000원 = new ProductRequest("피자", 18_000L, "http://example.com/pizza.jpg");
    public static final int 존재하지_않는_상품_아이디 = 5959;
}