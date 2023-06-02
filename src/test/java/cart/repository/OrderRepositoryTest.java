package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.inOrder;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.dto.OrderItemProductDto;
import cart.dao.entity.OrderEntity;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.exception.OrderNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    private OrderRepository orderRepository;

    @Mock
    private OrderItemDao orderItemDao;

    @Mock
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        this.orderRepository = new OrderRepository(orderDao, orderItemDao);
    }

    @Test
    @DisplayName("주문 정보와 주문된 상품들을 저장한다.")
    void save() {
        // given
        Member member = new Member(1L, "email", "password");
        given(orderDao.save(any(OrderEntity.class))).willReturn(1L);
        willDoNothing().given(orderItemDao).batchInsert(anyList());

        // when
        orderRepository.save(new Order(List.of(
            new OrderItem(
                new Product(1L, "상품명", Money.from(1000), "imgUrl"),
                Quantity.from(1))),
            Member.fromId(member.getId())));

        // then
        InOrder inOrder = inOrder(orderDao, orderItemDao);
        inOrder.verify(orderDao).save(any(OrderEntity.class));
        inOrder.verify(orderItemDao).batchInsert(anyList());
    }

    @Test
    @DisplayName("상세 주문 내역을 조회할 수 있다.")
    void findById() {
        // given
        long memberId = 1L;
        LocalDateTime time = LocalDateTime.of(2023, 6, 1, 12, 41, 0);
        Timestamp createdAt = Timestamp.valueOf(time);
        given(orderDao.findById(anyLong())).willReturn(
            Optional.of(new OrderEntity(1L, memberId, createdAt)));
        given(orderItemDao.findAllByOrderId(1L)).willReturn(
            List.of(
                new OrderItemProductDto(1L, 1L, 1L, 2, "치킨", 10_000, "chickenImg"),
                new OrderItemProductDto(1L, 2L, 2L, 1, "피자", 20_000, "pizzaImg")));

        // when
        Order order = orderRepository.findById(1L);

        // then
        assertThat(order).usingRecursiveComparison().isEqualTo(new Order(
            1L,
            List.of(
                new OrderItem(1L, new Product(1L, "치킨", Money.from(10_000), "chickenImg"),
                    Quantity.from(2)),
                new OrderItem(2L, new Product(2L, "피자", Money.from(20_000), "pizzaImg"),
                    Quantity.from(1))),
            createdAt, Member.fromId(memberId)));
    }

    @Test
    @DisplayName("존재하지 않는 id의 주문 내역은 조회에 실패한다.")
    void findById_idNotFound_fail() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderRepository.findById(1L))
            .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("사용자의 전체 주문 내역을 조회할 수 있다.")
    void findAllByMemberId() {
        // given
        long memberId = 1L;
        Member member = new Member(1L, "email", "password");
        LocalDateTime time = LocalDateTime.of(2023, 6, 1, 12, 41, 0);
        Timestamp createdAt = Timestamp.valueOf(time);
        given(orderDao.findAllByMemberId(anyLong())).willReturn(
            List.of(
                new OrderEntity(1L, memberId, createdAt),
                new OrderEntity(2L, memberId, createdAt)));
        given(orderItemDao.findAllByOrderIds(List.of(1L, 2L))).willReturn(List.of(
            new OrderItemProductDto(1L, 1L, 1L, 2, "치킨", 10_000, "chickenImg"),
            new OrderItemProductDto(1L, 2L, 2L, 1, "피자", 20_000, "pizzaImg"),
            new OrderItemProductDto(2L, 4L, 1L, 4, "치킨", 10_000, "chickenImg")));

        // when
        List<Order> orders = orderRepository.findAllByMember(member);

        // then
        assertThat(orders).hasSize(2);
        assertThat(orders).usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new Order(1L,
                        List.of(
                            new OrderItem(1L,
                                new Product(1L, "치킨", Money.from(10_000), "chickenImg"),
                                Quantity.from(2)),
                            new OrderItem(2L, new Product(2L, "피자", Money.from(20_000), "pizzaImg"),
                                Quantity.from(1))), createdAt, Member.fromId(memberId)),
                    new Order(2L,
                        List.of(
                            new OrderItem(4L,
                                new Product(1L, "치킨", Money.from(10_000), "chickenImg"),
                                Quantity.from(4))), createdAt, Member.fromId(memberId))
                ));
    }

    @Test
    @DisplayName("조회할 주문 내역이 존재하지 않는다면 비어있는 리스트가 반환된다.")
    void findByMember_emptyList() {
        // given
        given(orderDao.findAllByMemberId(anyLong())).willReturn(Collections.emptyList());

        // when
        List<Order> orders = orderRepository.findAllByMember(
            new Member(1L, "email", "password"));

        // then
        assertThat(orders).isEmpty();
    }

}