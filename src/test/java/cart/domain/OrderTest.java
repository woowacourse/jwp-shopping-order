package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("할인을 적용하지 않은 순수 주문된 원래 금액만 계산한다.")
    @Test
    void calculateTotalItemPrice() {
        // given
        Member member = new Member(1L, "jeomxon@gmail.com", "abcd1234@");
        OrderItem 치킨 = new OrderItem("치킨", 10_000, "image.url", 3, 0);
        OrderItem 피자 = new OrderItem("피자", 20_000, "image.url", 2, 0);
        List<OrderItem> orderItems = List.of(치킨, 피자);

        Order order = new Order(orderItems, 0, member);

        // when
        int totalPrice = order.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(70_000);
    }

    @DisplayName("상품 할인이 적용된 주문 금액을 반환한다.")
    @Test
    void calculateTotalItemDiscountAmount() {
        // given
        Member member = new Member(1L, "jeomxon@gmail.com", "abcd1234@");
        OrderItem 치킨 = new OrderItem("치킨", 10_000, "image.url", 3, 10);
        OrderItem 피자 = new OrderItem("피자", 20_000, "image.url", 2, 5);
        List<OrderItem> orderItems = List.of(치킨, 피자);

        Order order = new Order(orderItems, 0, member);

        // when
        int totalItemDiscountAmount = order.calculateTotalItemDiscountAmount();

        // then
        assertThat(totalItemDiscountAmount).isEqualTo(5_000);
    }

    @DisplayName("멤버할인이 적용된 주문 금액을 반환한다.")
    @Test
    void calculateTotalMemberDiscountAmount() {
        // given
        Member member = new Member(1L, "jeomxon@gmail.com", "abcd1234@", Grade.GOLD, 230_000);
        OrderItem 치킨 = new OrderItem("치킨", 10_000, "image.url", 3, 0);
        OrderItem 피자 = new OrderItem("피자", 20_000, "image.url", 2, 0);
        List<OrderItem> orderItems = List.of(치킨, 피자);

        Order order = new Order(orderItems, 0, member);

        // when
        int totalMemberDiscountAmount = order.calculateTotalMemberDiscountAmount();

        // then
        assertThat(totalMemberDiscountAmount).isEqualTo(7_000);
    }

    @DisplayName("멤버할인만 있는 경우 배송비를 제외한 최종 금액을 계산한다.")
    @Test
    void calculateDiscountedTotalItemPriceWhenMemberGrade() {
        // given
        Member member = new Member(1L, "jeomxon@gmail.com", "abcd1234@", Grade.GOLD, 230_000);
        OrderItem 치킨 = new OrderItem("치킨", 10_000, "image.url", 3, 0);
        OrderItem 피자 = new OrderItem("피자", 20_000, "image.url", 2, 0);
        List<OrderItem> orderItems = List.of(치킨, 피자);

        Order order = new Order(orderItems, 0, member);

        // when
        int discountedTotalItemPrice = order.calculateDiscountedTotalItemPrice();

        // then
        assertThat(discountedTotalItemPrice).isEqualTo(63_000);
    }

    @DisplayName("상품할인만 있는 경우 배송비를 제외한 최종 금액을 계산한다.")
    @Test
    void calculateDiscountedTotalItemPriceWhenPrice() {
        // given
        Member member = new Member(1L, "jeomxon@gmail.com", "abcd1234@");
        OrderItem 치킨 = new OrderItem("치킨", 10_000, "image.url", 3, 5);
        OrderItem 피자 = new OrderItem("피자", 20_000, "image.url", 2, 10);
        List<OrderItem> orderItems = List.of(치킨, 피자);

        Order order = new Order(orderItems, 0, member);

        // when
        int discountedTotalItemPrice = order.calculateDiscountedTotalItemPrice();

        // then
        assertThat(discountedTotalItemPrice).isEqualTo(64_500);
    }

    @DisplayName("멤버할인과 상품할인이 동시에 있는 경우 배송비를 제외한 상품할인만 적용된 최종 금액을 계산한다.")
    @Test
    void calculateDiscountedTotalItemPrice() {
        // given
        Member member = new Member(1L, "jeomxon@gmail.com", "abcd1234@", Grade.GOLD, 230_000);
        OrderItem 치킨 = new OrderItem("치킨", 10_000, "image.url", 3, 5);
        OrderItem 피자 = new OrderItem("피자", 20_000, "image.url", 2, 10);
        List<OrderItem> orderItems = List.of(치킨, 피자);

        Order order = new Order(orderItems, 0, member);

        // when
        int discountedTotalItemPrice = order.calculateDiscountedTotalItemPrice();

        // then
        assertThat(discountedTotalItemPrice).isEqualTo(64_500);
    }

    @DisplayName("최종 가격에 따른 배송비를 계산한다.")
    @Test
    void calculateShippingFee() {
        // given
        Member member = new Member(1L, "jeomxon@gmail.com", "abcd1234@", Grade.GOLD, 230_000);
        OrderItem 치킨 = new OrderItem("치킨", 10_000, "image.url", 3, 5);
        OrderItem 피자 = new OrderItem("피자", 20_000, "image.url", 2, 10);
        List<OrderItem> orderItems = List.of(치킨, 피자);

        Order order = new Order(orderItems, 0, member);

        // when
        int shippingFee = order.calculateShippingFee();

        // then
        assertThat(shippingFee).isEqualTo(0);
    }
}
