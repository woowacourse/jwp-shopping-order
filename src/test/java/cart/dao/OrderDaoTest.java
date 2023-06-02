package cart.dao;

import cart.domain.Member;
import cart.domain.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
class OrderDaoTest {
    private OrderDao orderDao;
    private MemberDao memberDao;
    private OrderProductDao orderProductDao;
    private ProductDao productDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        orderProductDao = new OrderProductDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문을 저장한다.")
    void saveOrder() {
        Member member = memberDao.getMemberById(1L);
        final OrderEntity orderEntity = new OrderEntity(member.getId(), member.getId(), 10000, 10000, false);

        assertDoesNotThrow(() -> orderDao.saveOrder(orderEntity));
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void findAllByMember() {
        Member member = memberDao.getMemberById(1L);
        OrderEntity orderEntity = new OrderEntity(member.getId(), member.getId(), 10000, 10000, false);
        Long orderId = orderDao.saveOrder(orderEntity);
        assertAll(
                () -> assertThat(orderDao.findByOrderId(member.getId(), orderId).get().getOriginalPrice()).isEqualTo(10000),
                () -> assertThat(orderDao.findByOrderId(member.getId(), orderId).get().getConfirmState()).isEqualTo(false),
                () -> assertThat(orderDao.findByOrderId(member.getId(), orderId).get().getMemberId()).isEqualTo(member.getId())
        );
    }

    @Test
    @DisplayName("특정 사용자의 주문을 모두 조회한다.")
    void findAllByMemberId() {
        Member member = memberDao.getMemberById(1L);
        createProduct();
        Long savedOrderId = createOrder(member);
        saveOrderProduct(savedOrderId);

        List<OrderEntity> orders = orderDao.findAllByMemberId(member.getId());

        assertAll(
                () -> assertThat(orders.get(0).getMemberId()).isEqualTo(member.getId()),
                () -> assertThat(orders.get(0).getOriginalPrice()).isEqualTo(10000),
                () -> assertThat(orders.get(0).getConfirmState()).isFalse()
        );
    }

    @Test
    @DisplayName("특정 사용자의 특정 주문을 조회한다.")
    void findByOrderId() {
        Member member = memberDao.getMemberById(1L);
        createProduct();
        Long savedOrderId1 = createOrder(member);
        saveOrderProduct(savedOrderId1);
        Long savedOrderId2 = createOrder(member);
        saveOrderProduct(savedOrderId2);

        OrderEntity order = orderDao.findByOrderId(member.getId(), savedOrderId1).get();

        assertAll(
                () -> assertThat(order.getMemberId()).isEqualTo(member.getId()),
                () -> assertThat(order.getOriginalPrice()).isEqualTo(10000),
                () -> assertThat(order.getConfirmState()).isFalse()
        );
    }

    @Test
    @DisplayName("특정 사용자의 특정 주문을 취소한다.")
    void cancelOrder() {
        Member member = memberDao.getMemberById(1L);
        createProduct();
        Long savedOrderId1 = createOrder(member);
        saveOrderProduct(savedOrderId1);

        assertDoesNotThrow(() -> orderDao.deleteOrderById(savedOrderId1));
    }

    @Test
    @DisplayName("특정 사용자의 특정 주문을 확정한다.")
    void confirmOrder() {
        Member member = memberDao.getMemberById(1L);
        createProduct();
        Long savedOrderId1 = createOrder(member);
        saveOrderProduct(savedOrderId1);
        orderDao.confirmOrder(savedOrderId1, member.getId());
        assertThat(orderDao.checkConfirmState(savedOrderId1)).isTrue();
    }

    private void saveOrderProduct(Long savedOrderId) {
        List<OrderProductEntity> orderProducts = List.of(new OrderProductEntity(1L, "오션", "오션.com", 10000, 1, savedOrderId));

        orderProductDao.saveOrderProductsByOrderId(savedOrderId, orderProducts);
    }

    private Long createOrder(Member member) {
        OrderEntity orderEntity = new OrderEntity(member.getId(), 10000, 10000);
        Long savedOrderId = orderDao.saveOrder(orderEntity);
        return savedOrderId;
    }

    private void createProduct() {
        Product product = new Product("오션", 10000, "오션.com");
        productDao.createProduct(product);
    }
}
