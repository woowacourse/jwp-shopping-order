package cart.order.domain;

import static cart.order.exception.OrderExceptionType.INVALID_ORDER_ITEM_PRODUCT_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cart.cartitem.domain.CartItem;
import cart.common.execption.BaseExceptionType;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.RateDiscountPolicy;
import cart.coupon.domain.SpecificCouponType;
import cart.member.domain.Member;
import cart.order.exception.OrderException;
import cart.product.domain.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderItem 은(는)")
class OrderItemTest {

    @Test
    void 주문_상품의_가격은_상품_단일_가격과_수량의_곱과_같다() {
        // given
        Product product = new Product("말랑", 100, "image");
        OrderItem orderItem = new OrderItem(
                10,
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getPrice(),
                product.getImageUrl());

        // when & then
        assertThat(orderItem.price()).isEqualTo(1000);
    }

    @Test
    void 수량은_양수여야_한다() {
        // given
        Product product = new Product("말랑", 100, "image");

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                new OrderItem(
                        0,
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getPrice(),
                        product.getImageUrl())
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(INVALID_ORDER_ITEM_PRODUCT_QUANTITY);
    }

    @Test
    void 주문상품의_가격은_상품가격에_쿠폰을_적용한_값이다() {
        // given
        Product product = new Product(10L, "말랑", 1000, "image");
        Member mallang = new Member(1L, "mallang", "!234");
        CartItem cartItem = new CartItem(product, mallang);
        OrderItem orderItem = OrderItem.withCoupon(cartItem, List.of(
                new Coupon("쿠폰",
                        new RateDiscountPolicy(30),
                        new SpecificCouponType(10L),
                        1L)));

        // when & then
        assertThat(orderItem.getOrderedProductPrice())
                .isEqualTo(700);
    }
}
