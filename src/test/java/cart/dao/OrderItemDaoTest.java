package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private TestFixture testFixture;
    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
        this.testFixture = new TestFixture(jdbcTemplate);
    }

    @DisplayName("주문 상품을 생성한다.")
    @Test
    void insert() {
        //given
        final Member member = testFixture.createMember("email", "password");
        final Product product = testFixture.createProduct("test", 1000, "testImageUrl");
        final Order order = testFixture.createOrder(member, product);

        //when
        final OrderItem persistedOrderItem = orderItemDao.insert(order.getId(), OrderItem.notPersisted(product, 10));

        //then
        assertAll(
            () -> assertThat(persistedOrderItem.getProduct()).isEqualTo(product),
            () -> assertThat(persistedOrderItem.getQuantity()).isEqualTo(10),
            () -> assertThat(persistedOrderItem.getId()).isNotNull()
        );
    }

    @DisplayName("주문 상품 여러개를 생성한다.")
    @Test
    void insertAll() {
        //given
        final Member member = testFixture.createMember("email", "password");
        final Product product = testFixture.createProduct("test", 1000, "testImageUrl");
        final Product product2 = testFixture.createProduct("test2", 2000, "testImageUrl2");
        final Order order = testFixture.createOrder(member, product);

        //when
        final int[] affectedRows = orderItemDao.insertAll(order.getId(), List.of(OrderItem.notPersisted(product, 10),
            OrderItem.notPersisted(product2, 10)));

        //then
        Assertions.assertThat(affectedRows).hasSize(2);
        Assertions.assertThat(affectedRows[0]).isOne();
        Assertions.assertThat(affectedRows[1]).isOne();
    }
}
