package cart.application;

import static cart.TestSource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cart.application.dto.GetOrdersRequest;
import cart.dao.OrderDao;
import cart.domain.Order;
import cart.domain.Paginator;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private Paginator paginator;
    @InjectMocks
    private OrderService orderService;

    @Test
    void 사용자의_주문_이력을_가져온다() {
        // given
        List<Order> orders = List.of(order1, order2, order3);
        given(orderDao.findAllByMemberId(anyLong())).willReturn(orders);
        given(paginator.paginate(anyList(), anyInt())).willReturn(orders);

        // when & then
        assertDoesNotThrow(() -> orderService.getOrdersWithPagination(member1, new GetOrdersRequest(1)));
    }
}
