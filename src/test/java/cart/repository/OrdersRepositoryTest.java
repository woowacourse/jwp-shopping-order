package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrdersDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static cart.fixture.Fixture.ORDERS;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrdersRepositoryTest {
    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private OrdersDao ordersDao;
    @InjectMocks
    private OrdersRepository ordersRepository;

    @Test
    @DisplayName("주문을 받는다")
    void takeOrders() {
        Mockito.when(ordersDao.createOrders(ORDERS.getMemberId(),ORDERS.getOriginalPrice(),ORDERS.getDiscountPrice())).thenReturn(1L);
        Assertions.assertThat(ordersRepository.takeOrders(ORDERS)).isEqualTo(1L);
        for (long id : ORDERS.getCartId()) {
            Mockito.verify(cartItemDao, times(1)).deleteById(id);
        }
    }
}
