package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Point;
import cart.dto.cartitem.CartItemResponse;
import cart.dto.order.OrderRequest;
import cart.exception.IllegalPointException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"classpath:/schema.sql", "classpath:/init_cart_item.sql"})
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private PointService pointService;

    private final Member memberWithPoint1000 = new Member(1L, "a@a.com", "1234");

    @Test
    @DisplayName("주문 시 사용할 포인트 검증에 실패하면 주문 데이터도 저장되지 않는다.")
    void createOrder_pointValidationFail_orderFail() {
        // when
        Exception exception = assertThrows(Exception.class,
            () -> orderService.createOrder(new OrderRequest(List.of(1L), 1500),
                memberWithPoint1000));

        //  then
        assertThat(exception).isInstanceOf(IllegalPointException.class);
        assertThat(findOrderOfMember(memberWithPoint1000))
            .isEmpty();
        assertThat(findCartItemsOfMember(memberWithPoint1000))
            .hasSize(2);
    }

    @Test
    @DisplayName("주문 시 사용할 포인트 검증에 성공하면 주문 데이터와 포인트 내역이 저장된다.")
    void createOrder_pointValidationSuccess_orderSuccess() {
        // when
        orderService.createOrder(new OrderRequest(List.of(1L), 500), memberWithPoint1000);

        // then
        assertThat(findOrderOfMember(memberWithPoint1000)).hasSize(1);
        assertThat(findCartItemsOfMember(memberWithPoint1000)).hasSize(1);
        assertThat(findPointOfMember(memberWithPoint1000)).isEqualTo(new Point(988));
    }

    List<Order> findOrderOfMember(Member member) {
        return orderService.findAllByMember(member);
    }

    List<CartItemResponse> findCartItemsOfMember(Member member) {
        return cartItemService.findByMember(member);
    }

    Point findPointOfMember(Member member) {
        return pointService.findPointOfMember(member);
    }
}