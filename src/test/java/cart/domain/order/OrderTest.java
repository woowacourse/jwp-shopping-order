package cart.domain.order;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.product.Item;
import cart.domain.product.Product;
import cart.exception.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cart.exception.ErrorCode.NOT_AUTHORIZATION_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTest {

    private Member member;
    private Coupon coupon;
    private final List<Item> items = new ArrayList<>();

    @BeforeEach
    void setUp() {
        member = new Member(1L, "name", "password");
        coupon = new Coupon("coupon", 10, 365, LocalDateTime.MAX);

        Product chicken = new Product("치킨", 1000, "http://example.com/chicken.jpg");
        Product pizza = new Product("피자", 2000, "http://example.com/pizza.jpg");
        Item chickenItem = new Item(chicken, 6);
        Item pizzaItem = new Item(pizza, 2);

        items.add(chickenItem);
        items.add(pizzaItem);
    }

    @DisplayName("Order가 사용자의 소유인지 확인한다.")
    @Test
    void checkOwner() {
        // given
        Order order = new Order(member, items, coupon);
        Member differentMember = new Member(2L, "a@a.com", "1234");

        // when & then
        assertAll(
                () -> assertDoesNotThrow(() -> order.checkOwner(member)),
                () -> assertThatThrownBy(() -> order.checkOwner(differentMember))
                        .isInstanceOf(AuthorizationException.class)
                        .extracting("errorCode")
                        .isEqualTo(NOT_AUTHORIZATION_MEMBER)
        );
    }

    @DisplayName("할인된 후의 상품 총 가격을 계산한다.")
    @Test
    void getDiscountedTotalPrice() {
        // given
        Order order = new Order(member, items, coupon);

        // when
        int discountedTotalPrice = order.getDiscountedTotalPrice();

        // then
        assertThat(discountedTotalPrice).isEqualTo(10_000 - 1000);
    }

    @DisplayName("할인된 가격을 계산한다.")
    @Test
    void getCouponDiscountPrice() {
        // given
        Order order = new Order(member, items, coupon);

        // when
        int couponDiscountPrice = order.getCouponDiscountPrice();

        // then
        assertThat(couponDiscountPrice).isEqualTo(1000);
    }
}
