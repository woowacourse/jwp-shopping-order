package cart.infrastructure.repository;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.밀리_쿠폰_10퍼센트;
import static cart.fixture.TestFixture.장바구니_밀리_치킨_10개;
import static cart.fixture.TestFixture.장바구니_밀리_피자_1개;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cart.domain.Order;
import cart.domain.coupon.CouponType;
import cart.infrastructure.dao.CouponDao;
import cart.infrastructure.dao.MemberDao;
import cart.infrastructure.dao.OrderDao;
import cart.infrastructure.dao.OrderProductDao;
import cart.infrastructure.entity.CouponEntity;
import cart.infrastructure.entity.MemberEntity;
import cart.infrastructure.entity.OrderEntity;
import cart.infrastructure.entity.OrderProductEntity;
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
    private JdbcOrderRepository orderRepository;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderProductDao orderProductDao;

    @Mock
    private MemberDao memberDao;

    @Mock
    private CouponDao couponDao;

    @Test
    void 주문을_저장한다() {
        given(orderDao.save(any()))
                .willReturn(1L);

        Order order = Order.of(밀리, List.of(장바구니_밀리_치킨_10개, 장바구니_밀리_피자_1개), 3000, 밀리_쿠폰_10퍼센트);
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
                .willReturn(Optional.of(new OrderEntity(1L, 1L, 1L, "20230616052900331", 3000,
                        LocalDateTime.of(2023, 6, 16, 5, 29, 0, 33))));
        given(orderProductDao.findByOrderId(anyLong()))
                .willReturn(List.of(
                        new OrderProductEntity(1L, 1L, 1L, 2, "피자", BigDecimal.valueOf(20000), "http://pizza.com"),
                        new OrderProductEntity(1L, 1L, 2L, 3, "치킨", BigDecimal.valueOf(10000), "http://chicken.com")
                ));
        given(memberDao.findById(any()))
                .willReturn(Optional.of(new MemberEntity(1L, "millie@email.com", "millie")));
        given(couponDao.findById(any()))
                .willReturn(Optional.of(new CouponEntity(1L, "쿠폰", "RATE", BigDecimal.valueOf(10), BigDecimal.ZERO)));

        Optional<Order> savedOrder = orderRepository.findById(1L);

        assertThat(savedOrder).isPresent();
        assertThat(savedOrder.get().getId()).isEqualTo(1L);
    }

    @Test
    void 주문을_조회할_때_쿠폰_id가_없으면_빈_쿠폰을_반환한다() {
        given(orderDao.findById(any()))
                .willReturn(Optional.of(new OrderEntity(1L, 1L, 0L, "20230616052900331", 3000,
                        LocalDateTime.of(2023, 6, 16, 5, 29, 0, 33))));
        given(orderProductDao.findByOrderId(anyLong()))
                .willReturn(List.of(
                        new OrderProductEntity(1L, 1L, 1L, 2, "피자", BigDecimal.valueOf(20000), "http://pizza.com"),
                        new OrderProductEntity(1L, 1L, 2L, 3, "치킨", BigDecimal.valueOf(10000), "http://chicken.com")
                ));
        given(memberDao.findById(any()))
                .willReturn(Optional.of(new MemberEntity(1L, "millie@email.com", "millie")));

        Optional<Order> savedOrder = orderRepository.findById(1L);

        verify(couponDao, never()).findById(any());
        assertThat(savedOrder).isPresent();
        assertThat(savedOrder.get().getCoupon().getCouponType()).isEqualTo(CouponType.NONE);
    }

    @Test
    void 사용자의_전체_주문을_조회한다() {
        given(orderDao.findAllByMemberId(밀리.getId()))
                .willReturn(
                        List.of(
                                new OrderEntity(1L, 1L, 1L, "20230616052900331", 3000,
                                        LocalDateTime.of(2023, 6, 16, 5, 29, 0, 33)),
                                new OrderEntity(2L, 2L, 2L, "20230617052900331", 3000,
                                        LocalDateTime.of(2023, 6, 17, 5, 29, 0, 33))
                        )
                );
        given(orderProductDao.findByOrderId(anyLong()))
                .willReturn(
                        List.of(
                                new OrderProductEntity(1L, 1L, 1L, 2, "피자", BigDecimal.valueOf(20000),
                                        "http://pizza.com"),
                                new OrderProductEntity(2L, 1L, 2L, 3, "치킨", BigDecimal.valueOf(10000),
                                        "http://chicken.com")
                        ),
                        List.of(
                                new OrderProductEntity(3L, 2L, 1L, 20, "피자", BigDecimal.valueOf(20000),
                                        "http://pizza.com"),
                                new OrderProductEntity(4L, 2L, 2L, 30, "치킨", BigDecimal.valueOf(10000),
                                        "http://chicken.com")
                        )
                );

        given(couponDao.findById(any()))
                .willReturn(Optional.of(new CouponEntity(1L, "쿠폰", "RATE", BigDecimal.valueOf(10), BigDecimal.ZERO)));

        List<Order> orders = orderRepository.findAllByMember(밀리);

        assertThat(orders).hasSize(2);
        assertThat(orders).map(Order::getId)
                .containsExactly(1L, 2L);

    }
}
