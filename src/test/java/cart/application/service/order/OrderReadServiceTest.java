package cart.application.service.order;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import cart.application.repository.order.OrderRepository;
import cart.application.service.order.dto.OrderDto;
import cart.domain.Member;
import cart.domain.Point;
import cart.domain.PointHistory;
import cart.domain.coupon.Coupon;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.ui.MemberAuth;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderReadServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderReadService orderReadService;

    @Test
    @DisplayName("사용자의 모든 주문 목록을 조회한다.")
    void findAllByMember() {
        // given
        Order order = new Order(1L,
                new Member("test", "test@email.com", "test123"),
                List.of(new OrderItem(1L, "testItem", "testProduct", 10000, 1)),
                List.of(new Coupon(1L, "testCoupon", 1000, 10, 0)),
                9000,
                10000,
                new Point(List.of(new PointHistory(1L, 0, 0))).calculateTotalPoint(),
                "2023-06-03 16:34:58.703652"
        );
        given(orderRepository.findAllByMemberId(anyLong()))
                .willReturn(List.of(order));

        // when, then
        assertSoftly(softly -> {
            List<OrderDto> orderDtos = orderReadService.findAllByMember(
                    new MemberAuth(1L, "test", "test@email.com", "test123"));
            softly.assertThat(orderDtos).hasSize(1);
        });
    }

    @Test
    @DisplayName("사용자의 특정 주문을 상세조회한다.")
    void findByOrderId() {
        // given
        Order order = new Order(1L,
                new Member("test", "test@email.com", "test123"),
                List.of(new OrderItem(1L, "testItem", "testProduct", 10000, 1)),
                List.of(new Coupon(1L, "testCoupon", 1000, 10, 0)),
                9000,
                10000,
                new Point(List.of(new PointHistory(1L, 0, 0))).calculateTotalPoint(),
                "2023-06-03 16:34:58.703652"
        );

        given(orderRepository.findById(anyLong()))
                .willReturn(order);

        // when, then
        assertDoesNotThrow(() -> orderReadService.findByOrderId(
                new MemberAuth(1L, "test", "test@email.com", "test123"),
                1L));
    }

}
