package cart.repository;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.장바구니_밀리_치킨_10개;
import static cart.fixture.TestFixture.장바구니_밀리_피자_1개;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.domain.Order;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    @InjectMocks
    private OrderRepository orderRepository;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderProductDao orderProductDao;

    @Test
    void 주문을_저장한다() {
        given(orderDao.save(any()))
                .willReturn(1L);

        Order order = Order.of(밀리, List.of(장바구니_밀리_치킨_10개, 장바구니_밀리_피자_1개), 3000);
        Order savedOrder = orderRepository.save(order);

        verify(orderProductDao, times(1)).saveAll(any());
        assertThat(savedOrder.getId()).isEqualTo(1L);
        assertThat(savedOrder).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(order);
    }

}
