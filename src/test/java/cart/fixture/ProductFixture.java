package cart.fixture;

import cart.domain.Product;
import cart.domain.vo.Money;
import cart.domain.vo.Name;

public abstract class ProductFixture {

    public static Product 상품(Long 상품_식별자값, String 상품명, int 상품_가격, String 상품_이미지_주소) {
        return new Product(상품_식별자값, Name.from(상품명), Money.from(상품_가격), 상품_이미지_주소);
    }

    public static Product 상품(Long 상품_식별자값, String 상품명, String 상품_가격, String 상품_이미지_주소) {
        return new Product(상품_식별자값, Name.from(상품명), Money.from(상품_가격), 상품_이미지_주소);
    }

    public static Product 상품(String 상품명, String 상품_가격, String 상품_이미지_주소) {
        return new Product(Name.from(상품명), Money.from(상품_가격), 상품_이미지_주소);
    }
}
