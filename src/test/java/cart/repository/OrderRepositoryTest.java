package cart.repository;

import static java.lang.Long.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.inOrder;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.cart.Quantity;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.Orders;
import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.ProductEntity;
import cart.exception.OrderException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    private OrderRepository orderRepository;

    @Mock
    private MemberDao memberDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private OrderItemDao orderItemDao;

    @Mock
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository(memberDao, productDao, orderItemDao, orderDao);
    }

    @Test
    void 주문정보와_주문된_상품을_저장한다() {
        // given
        Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final OrderItem orderItem = new OrderItem(new Quantity(5),
                new Product(1L, new Name("상품명"), new ImageUrl("img.com"), new Price(1000)));
        given(orderDao.save(any(OrderEntity.class))).willReturn(1L);
        willDoNothing().given(orderItemDao).batchInsert(anyList());

        // when
        orderRepository.save(new Order(member, List.of(orderItem)));

        // then
        InOrder inOrder = inOrder(orderDao, orderItemDao);
        inOrder.verify(orderDao).save(any(OrderEntity.class));
        inOrder.verify(orderItemDao).batchInsert(anyList());
    }

    @Test
    void 상세_주문_내역을_조회할_수_있다() {
        // given
        final OrderEntity orderEntity = new OrderEntity(1L, 1L,
                Timestamp.valueOf(LocalDateTime.of(2023, 6, 1, 12, 41, 0)));
        final List<OrderItemEntity> orderItemEntities = List.of(new OrderItemEntity(1L, 1L, 1L, 2000, 5));
        final ProductEntity productEntity = new ProductEntity(1L, "상품", "image.com", 1000);
        final Map<Long, ProductEntity> productGroupById = Map.of(1L, productEntity);
        final MemberEntity memberEntity = new MemberEntity(1L, "a@a.com", "1234");

        given(orderDao.findById(1L)).willReturn(Optional.of(orderEntity));
        given(orderItemDao.findByOrderId(1L)).willReturn(orderItemEntities);
        given(productDao.getProductGroupById(List.of(1L))).willReturn(productGroupById);
        given(memberDao.getMemberById(1L)).willReturn(Optional.of(memberEntity));

        // when
        final Order order = orderRepository.findByOrderId(1L);

        // then
        assertThat(order).usingRecursiveComparison().isEqualTo(
                new Order(1L, new Member(1L, new Email("a@a.com"), new Password("1234")),
                        Timestamp.valueOf(LocalDateTime.of(2023, 6, 1, 12, 41, 0)),
                        List.of(new OrderItem(1L, new Quantity(5),
                                new Product(1L, new Name("상품"), new ImageUrl("image.com"), new Price(2000))))));
    }

    @Test
    void 존재하지_않는_ID로_조회하면_예외가_발생한다() {
        // given
        given(orderDao.findById(MAX_VALUE)).willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> orderRepository.findByOrderId(MAX_VALUE))
                .isInstanceOf(OrderException.NotFound.class);
    }

    @Test
    void 유저의_전체_주문_목록을_조회할_수_있다() {
        // given
        final Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final MemberEntity memberEntity = new MemberEntity(1L, "a@a.com", "1234");
        final OrderEntity orderEntity = new OrderEntity(1L, 1L,
                Timestamp.valueOf(LocalDateTime.of(2023, 6, 1, 12, 41, 0)));
        final List<OrderItemEntity> orderItemEntities = List.of(new OrderItemEntity(1L, 1L, 1L, 2000, 5));
        final Map<Long, List<OrderItemEntity>> orderItemGroupByOrderId = Map.of(1L, orderItemEntities);
        final ProductEntity productEntity = new ProductEntity(1L, "상품", "image.com", 1000);
        final Map<Long, ProductEntity> productGroupById = Map.of(1L, productEntity);

        given(memberDao.getMemberById(1L)).willReturn(Optional.of(memberEntity));
        given(orderDao.findOrderByMemberId(1L)).willReturn(List.of(orderEntity));
        given(orderItemDao.findGroupByOrderId(List.of(1L))).willReturn(orderItemGroupByOrderId);
        given(productDao.getProductGroupById(List.of(1L))).willReturn(productGroupById);

        // when
        final Orders orders = orderRepository.findByMember(member);

        // then
        assertThat(orders).usingRecursiveComparison().isEqualTo(new Orders(
                List.of(new Order(1L, new Member(1L, new Email("a@a.com"), new Password("1234")),
                        Timestamp.valueOf(LocalDateTime.of(2023, 6, 1, 12, 41, 0)),
                        List.of(new OrderItem(1L, new Quantity(5),
                                new Product(1L, new Name("상품"), new ImageUrl("image.com"), new Price(2000))))))));
    }

    @Test
    void 유저의_전체_주문_목록이_없을_때_조회하면_빈_주문을_반환한다() {
        // given
        final Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        given(memberDao.getMemberById(1L)).willReturn(Optional.of(new MemberEntity(1L, "a@a.com", "1234")));
        given(orderDao.findOrderByMemberId(1L)).willReturn(Collections.emptyList());

        // when
        assertThat(orderRepository.findByMember(member)).usingRecursiveComparison()
                .isEqualTo(new Orders(Collections.emptyList()));
    }
}
