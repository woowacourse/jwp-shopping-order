package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrdersDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {
    @InjectMocks
    private OrdersService ordersService;
    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private OrdersDao ordersDao;

    @Test
    @DisplayName("주문을 받는다")
    void takeOrders() {
    }
}
