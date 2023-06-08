package fixture;

import static fixture.ProductFixture.상품_치킨;
import static fixture.ProductFixture.상품_샐러드;
import static fixture.ProductFixture.상품_피자;

import cart.domain.OrderProduct;
import cart.domain.Quantity;

public class OrdersProductFixture {

    public static final OrderProduct 주문_상품_치킨_2개 = new OrderProduct(1L, 상품_치킨, Quantity.from(2));
    public static final OrderProduct 주문_상품_샐러드_2개 = new OrderProduct(2L, 상품_샐러드, Quantity.from(2));
    public static final OrderProduct 주문_상품_피자_2개 = new OrderProduct(3L, 상품_피자, Quantity.from(2));
    public static final OrderProduct 주문_상품_치킨_2개_2 = new OrderProduct(4L, 상품_치킨, Quantity.from(2));
    public static final OrderProduct 주문_상품_치킨_2개_3 = new OrderProduct(5L, 상품_치킨, Quantity.from(2));
    public static final OrderProduct 주문_상품_치킨_2개_4 = new OrderProduct(6L, 상품_치킨, Quantity.from(2));

}
