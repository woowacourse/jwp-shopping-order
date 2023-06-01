package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrdersDao;
import cart.domain.Member;
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
    private OrdersDao ordersDao;
    @InjectMocks
    private OrdersRepository ordersRepository;

    @Test
    @DisplayName("주문을 받는다")
    void takeOrders() {
        Mockito.when(ordersDao.createOrders(1l,2000)).thenReturn(1l);
        Assertions.assertThat(ordersRepository.takeOrders(1l,2000)).isEqualTo(1L);
    }

    @Test
    @DisplayName("사용자의 모든 주문을 찾는다")
    void findAllOrdersByMember(){
        Mockito.when(ordersDao.findAllByMemberId(1l)).thenReturn(List.of());
        Assertions.assertThatNoException()
                .isThrownBy(()->ordersRepository.findAllOrdersByMember(new Member(1l,"test","test")));
    }
}
