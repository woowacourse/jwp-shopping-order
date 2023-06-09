package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.domain.Product;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {


    @DisplayName("DB에 저장되지 않은 주문을 생성한다.")
    @Test
    void notPersisted() {
        //given
        final Member member = new Member(null, "email", "password");
        final OrderItem orderItem = OrderItem.notPersisted(new Product("product", 1000, "imageUrl"), 10);
        final OrderItems orderItems = new OrderItems(List.of(orderItem));
        final OrderPrice orderPrice = OrderPrice.of(1L, 1L, 1L, 1L);
        final LocalDateTime now = LocalDateTime.of(2022, 4, 2, 2, 2);

        //when
        final Order order = Order.notPersisted(member, orderItems, orderPrice, now);

        //then
        assertAll(
            () -> assertThat(order.getMember()).usingRecursiveComparison().isEqualTo(member),
            () -> assertThat(order.getOrderItems()).containsOnly(orderItem),
            () -> assertThat(order.getProductPrice()).isEqualTo(orderItems.getTotalPrice()),
            () -> assertThat(order.getOrderTime()).isEqualTo(now)
        );
    }

    @DisplayName("DB에 저장된 주문을 생성한다.")
    @Test
    void persisted() {
        //given
        final Member member = new Member(null, "email", "password");
        final OrderItem orderItem = OrderItem.notPersisted(new Product("product", 1000, "imageUrl"), 10);
        final OrderItems orderItems = new OrderItems(List.of(orderItem));
        final OrderPrice orderPrice = OrderPrice.of(1L, 1L, 1L, 1L);
        final LocalDateTime now = LocalDateTime.of(2022, 4, 2, 2, 2);

        //when
        final Order order = Order.persisted(1L, member, orderItems, orderPrice, now);

        //then
        assertAll(
            () -> assertThat(order.getId()).isEqualTo(1L),
            () -> assertThat(order.getMember()).usingRecursiveComparison().isEqualTo(member),
            () -> assertThat(order.getOrderItems()).containsOnly(orderItem),
            () -> assertThat(order.getProductPrice()).isEqualTo(orderItems.getTotalPrice()),
            () -> assertThat(order.getOrderTime()).isEqualTo(now)
        );
    }
}
