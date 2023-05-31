package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrdersDao;
import cart.dao.entity.CartItemEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


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
//        Mockito.when(cartItemDao.findCartItemEntitiesByCartId(1L)).thenReturn(new CartItemEntity(1L,1L,1L,1));
//        Mockito.when(ordersDao.createOrders(1L, 2000,1900)).thenReturn(1L);
//        Assertions.assertThat(ordersRepository.takeOrders(1L,List.of(1L),2000,1900,List.of(1L))).isEqualTo(1L);
    }
}
