package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.coupon.MemberCoupon;
import cart.exception.StoreException;
import cart.exception.forbidden.ForbiddenException;
import cart.fixture.CouponFixture.금액_10000원이상_1000원할인;
import cart.fixture.MemberFixture.Member_test1;
import cart.fixture.MemberFixture.Member_test2;
import cart.fixture.ProductFixture.치킨_15000원;
import cart.fixture.ProductFixture.피자_20000원;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Nested
    class 주문_생성시_ {

        Member member = Member_test1.getMemberOf(1L);
        Member other = Member_test2.getMemberOf(2L);
        Product product1 = 피자_20000원.PRODUCT;
        Product product2 = 치킨_15000원.PRODUCT;
        CartItems cartItems = new CartItems(List.of(new CartItem(member, product1), new CartItem(member, product2)));
        MemberCoupon memberCoupon = new MemberCoupon(금액_10000원이상_1000원할인.COUPON, member, LocalDateTime.MAX);

        @Nested
        class 장바구니_상품_검증_ {

            @Test
            void 본인의_장바구니_상품이_아니면_예외발생() {
                // given
                cartItems = new CartItems(List.of(new CartItem(member, product1), new CartItem(other, product2)));

                // when then
                assertThatThrownBy(() -> Order.of(member, cartItems, memberCoupon))
                        .isInstanceOf(ForbiddenException.class);
            }

            @Test
            void 장바구니가_비어있으면_예외발생() {
                // given
                cartItems = new CartItems(Collections.emptyList());

                // when then
                assertThatThrownBy(() -> Order.of(member, cartItems, memberCoupon))
                        .isInstanceOf(StoreException.class)
                        .hasMessage("장바구니 상품이 없습니다.");
            }
        }

        @Nested
        class 쿠폰_검증_ {

            @Test
            void 본인의_쿠폰이_아니면_예외발생() {
                // given
                memberCoupon = new MemberCoupon(금액_10000원이상_1000원할인.COUPON, other, LocalDateTime.MAX);

                // when then
                assertThatThrownBy(() -> Order.of(member, cartItems, memberCoupon))
                        .isInstanceOf(ForbiddenException.class);
            }

            @Test
            void 유효기간이_지난_쿠폰이면_예외발생() {
                // given
                memberCoupon = new MemberCoupon(금액_10000원이상_1000원할인.COUPON, member, LocalDateTime.MIN);

                // when then
                assertThatThrownBy(() -> Order.of(member, cartItems, memberCoupon))
                        .isInstanceOf(StoreException.class)
                        .hasMessage("적용할 수 없는 쿠폰입니다.");
            }


            @Test
            void 이미_사용된_쿠폰이면_예외발생() {
                // given
                memberCoupon = new MemberCoupon(1L, 금액_10000원이상_1000원할인.COUPON, member, true, LocalDateTime.MAX,
                        LocalDateTime.now());

                // when then
                assertThatThrownBy(() -> Order.of(member, cartItems, memberCoupon))
                        .isInstanceOf(StoreException.class)
                        .hasMessage("적용할 수 없는 쿠폰입니다.");
            }

            @Test
            void 쿠폰의_최소적용금액보다_상품총합이_적으면_예외발생() {
                // given
                cartItems = new CartItems(List.of(new CartItem(member, new Product("피자", 9000, "image"))));

                // when then
                assertThatThrownBy(() -> Order.of(member, cartItems, memberCoupon))
                        .isInstanceOf(StoreException.class)
                        .hasMessage("적용할 수 없는 쿠폰입니다.");
            }
        }

        @Nested
        class 배송비_검증_ {

            @Test
            void 할인_적용된_가격이_30000원보다_적으면_배송비포함() {
                // given
                int productPrice = 30_000;
                cartItems = new CartItems(
                        List.of(new CartItem(member, new Product("피자", productPrice, "image"))));

                // when
                Order order = Order.of(member, cartItems, memberCoupon);

                // then
                assertThat(order.getShippingFee().getValue()).isEqualTo(3_000);
            }

            @Test
            void 할인_적용된_가격이_30000원_이상이면_배송비무료() {
                // given
                int productPrice = 31_000;
                CartItems cartItems = new CartItems(
                        List.of(new CartItem(member, new Product("피자", productPrice, "image"))));
                // when
                Order order = Order.of(member, cartItems, memberCoupon);

                // then
                assertThat(order.getShippingFee().getValue()).isEqualTo(0);
            }
        }


        @Test
        void 주문_금액은_총_상품가격합에_할인가격을_빼고_배송비를_더한_금액이다() {
            // when
            cartItems = new CartItems(
                    List.of(new CartItem(member, new Product("피자", 30_000, "image"))));

            Order order = Order.of(member, cartItems, memberCoupon);

            // then
            assertThat(order.getTotalOrderPrice()).isEqualTo(30_000 - 1_000 + 3_000);
        }
    }
}
