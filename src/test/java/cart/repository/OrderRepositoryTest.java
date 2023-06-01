package cart.repository;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.장바구니_밀리_치킨_10개;
import static cart.fixture.TestFixture.장바구니_밀리_피자_1개;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.domain.Order;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderProductEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @Mock
    private MemberDao memberDao;

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

    @Test
    void 주문을_id로_조회한다() {
        given(orderDao.findById(any()))
                .willReturn(Optional.of(new OrderEntity(1L, 1L, "20230616052900331", 3000,
                        LocalDateTime.of(2023, 6, 16, 5, 29, 0, 33))));
        given(orderProductDao.findByOrderId(anyLong()))
                .willReturn(List.of(
                        new OrderProductEntity(1L, 1L, 1L, 2, "피자", BigDecimal.valueOf(20000), "http://pizza.com"),
                        new OrderProductEntity(1L, 1L, 2L, 3, "치킨", BigDecimal.valueOf(10000), "http://chicken.com")
                ));
        given(memberDao.findById(any()))
                .willReturn(Optional.of(new MemberEntity(1L, "millie@email.com", "millie")));

        Optional<Order> savedOrder = orderRepository.findById(1L);

        assertThat(savedOrder).isPresent();
        assertThat(savedOrder.get().getId()).isEqualTo(1L);
    }
}
