package cart.order;

import cart.order.application.OrderService;
import cart.order.dao.CartOrderDao;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderServiceTest {

    @InjectMocks
    private OrderService cartOrderService;

    @Mock
    private CartOrderDao cartOrderDao;

    @Test
    void test() {

    }
}
