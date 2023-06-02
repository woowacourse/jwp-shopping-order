package cart.step2.order.service;

import cart.step2.order.domain.Order;
import cart.step2.order.domain.repository.OrderRepository;
import cart.step2.order.presentation.dto.OrderResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @DisplayName("입력받은 MemberId가 일치하는 Order들을 모두 조회해서 List<OrderResponse>로 반환한다.")
    @Test
    void findAllByMemberId() {
        // given
        LocalDateTime date1 = LocalDateTime.now();
        LocalDateTime date2 = LocalDateTime.now();
        List<Order> orders = List.of(
                Order.of(1L, 1000, 1L, 1L, date1, Collections.emptyList()),
                Order.of(2L, 2000, 2L, 1L, date2, Collections.emptyList())
        );
        doReturn(orders).when(orderRepository).findAllByMemberId(1L);

        // when
        List<OrderResponse> responses = orderService.findAllByMemberId(1L);

        // then
        Assertions.assertAll(
                () -> assertThat(responses).extracting(OrderResponse::getId)
                        .contains(1L, 2L),
                () -> assertThat(responses).extracting(OrderResponse::getPrice)
                        .contains(1000, 2000),
                () -> assertThat(responses).extracting(OrderResponse::getDate)
                        .contains(date1, date2),
                () -> assertThat(responses.get(0).getClass()).isEqualTo(OrderResponse.class)

        );
    }

}
