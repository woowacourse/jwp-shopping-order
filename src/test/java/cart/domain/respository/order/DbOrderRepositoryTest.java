package cart.domain.respository.order;

import cart.dao.OrderDao;
import cart.dao.TestFixture;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.order.Order;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class DbOrderRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private DbOrderRepository dbOrderRepository;
    private TestFixture testFixture;

    @BeforeEach
    void setUp() {
        this.dbOrderRepository = new DbOrderRepository(new OrderDao(jdbcTemplate));
        this.testFixture = new TestFixture(jdbcTemplate);
    }

    @DisplayName("해당 ID의 주문을 조회한다.")
    @Test
    void findByOrderId() {
        //given
        final Member member = testFixture.createMember("email", "password");
        final Product product = testFixture.createProduct("product", 1000, "imageUrl");
        final Order order = testFixture.createOrderAndInsertOrderItem(member, product);

        //when
        final Order findOrder = dbOrderRepository.findByOrderId(order.getId());

        //then
        Assertions.assertThat(findOrder.getId()).isEqualTo(order.getId());
    }

    @DisplayName("해당 ID의 멤버의 모든 주문을 조회한다.")
    @Test
    void findAllByMemberId() {
        //given
        final Member member = testFixture.createMember("email", "password");
        final Product product = testFixture.createProduct("product", 1000, "imageUrl");
        final Order order = testFixture.createOrderAndInsertOrderItem(member, product);

        //when
        final List<Order> orders = dbOrderRepository.findAllByMemberId(member.getId());

        //then
        Assertions.assertThat(orders).hasSize(1);
        Assertions.assertThat(orders.get(0).getId()).isEqualTo(order.getId());
    }
}
