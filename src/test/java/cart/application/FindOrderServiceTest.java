package cart.application;

import static cart.TestSource.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cart.application.dto.GetOrdersRequest;
import cart.domain.Order;
import cart.domain.Page;
import cart.domain.Paginator;
import cart.exception.AuthenticationException;
import cart.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FindOrderServiceTest {

    @Mock
    private Paginator<Order> paginator;
    @Mock
    private PointService pointService;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private FindOrderService findOrderService;

    @Test
    void 사용자의_주문_이력을_가져온다() {
        // given
        List<Order> orders = List.of(order1, order2, order3);
        Page<Order> pages = new Page<>(orders, 3, 1, 1);
        given(orderRepository.findAllByMemberId(anyLong())).willReturn(orders);
        given(paginator.paginate(anyList(), anyInt())).willReturn(pages);

        // when & then
        assertDoesNotThrow(() -> findOrderService.getOrdersWithPagination(member1, new GetOrdersRequest(1)));
    }

    @Nested
    class 특정_주문을_상세_조회한다 {

        @Test
        void 다른_사용자의_주문을_조회하면_예외가_발생한다() {
            // given
            given(orderRepository.findById(orderByMember1.getId())).willReturn(orderByMember1);

            // when & then
            assertThatThrownBy(() -> findOrderService.getOrder(member2, orderByMember1.getId()))
                .isInstanceOf(AuthenticationException.class);
        }

        @Test
        void 사용자의_특정_주문을_상세_조회한다() {
            // given
            given(orderRepository.findById(orderByMember1.getId())).willReturn(orderByMember1);
            given(pointService.getIncreasedPointAmountByMemberId(member1.getId())).willReturn(0);
            given(pointService.getUsedPointAmountByMemberId(member1.getId())).willReturn(0);

            // when & then
            assertDoesNotThrow(() -> findOrderService.getOrder(member1, orderByMember1.getId()));
        }
    }
}
