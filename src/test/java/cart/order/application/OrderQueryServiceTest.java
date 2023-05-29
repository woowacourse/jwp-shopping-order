package cart.order.application;

import static cart.order.exception.OrderExceptionType.NO_AUTHORITY_QUERY_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import cart.common.execption.BaseExceptionType;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderRepository;
import cart.order.exception.OrderException;
import cart.order.presentation.dto.OrderResponse;
import cart.order.presentation.dto.OrderResponse.OrderItemResponse;
import cart.order.presentation.dto.OrderResponses;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderQueryService 은(는)")
class OrderQueryServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderQueryService orderQueryService;

    @Test
    void 회원의_모든_주문을_조회한다() {
        // given
        List<Order> orders = List.of(new Order(1L, 1L,
                        new OrderItem(1L, 10, 1L, "말랑", 1000, "image"),
                        new OrderItem(2L, 20, 2L, "코코닥", 2000, "image")),
                new Order(2L, 1L,
                        new OrderItem(3L, 10, 1L, "말랑", 1000, "image"),
                        new OrderItem(4L, 20, 2L, "코코닥", 2000, "image")));
        given(orderRepository.findAllByMemberId(1L))
                .willReturn(orders);

        // when
        OrderResponses orderResponses = orderQueryService.findAllByMemberId(1L);

        // then
        OrderResponses expected = new OrderResponses(List.of(
                new OrderResponse(1L,
                        List.of(new OrderItemResponse(1L, "말랑", 10000, 10, "image"),
                                new OrderItemResponse(2L, "코코닥", 40000, 20, "image")),
                        50000
                ),
                new OrderResponse(2L,
                        List.of(new OrderItemResponse(3L, "말랑", 10000, 10, "image"),
                                new OrderItemResponse(4L, "코코닥", 40000, 20, "image")),
                        50000
                )
        ));
        assertThat(orderResponses).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 회원의_특정_주문을_조회한다() {
        // given
        given(orderRepository.findById(1L))
                .willReturn(new Order(1L, 2L,
                        new OrderItem(1L, 10, 1L, "말랑", 1000, "image"),
                        new OrderItem(2L, 20, 2L, "코코닥", 2000, "image")));

        // when
        OrderResponse orderResponse = orderQueryService.findByIdAndMemberId(1L, 2L);

        // then
        OrderResponse expected = new OrderResponse(1L,
                List.of(new OrderItemResponse(1L, "말랑", 10000, 10, "image"),
                        new OrderItemResponse(2L, "코코닥", 40000, 20, "image")),
                50000
        );
        assertThat(orderResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 자신의_주문이_아닐경우_예외_발생() {
        // given
        given(orderRepository.findById(1L))
                .willReturn(new Order(1L, 2L,
                        new OrderItem(1L, 10, 1L, "말랑", 1000, "image"),
                        new OrderItem(2L, 20, 2L, "코코닥", 2000, "image")));

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                orderQueryService.findByIdAndMemberId(1L, 1L)
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(NO_AUTHORITY_QUERY_ORDER);
    }
}
