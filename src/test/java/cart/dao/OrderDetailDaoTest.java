package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import cart.entity.OrderDetailEntity;
import cart.fixture.OrderDetailEntityFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class OrderDetailDaoTest {

    private OrderDetailDao orderDetailDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        orderDetailDao = new OrderDetailDao(jdbcTemplate);
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql", "classpath:insertOrders.sql", "classpath:insertProduct.sql"})
    @Test
    void insert() {
        final OrderDetailEntity result = orderDetailDao.insert(OrderDetailEntityFixture.ODO1);
        assertAll(
                () -> assertThat(result.getId()).isPositive(),
                () -> assertThat(result.getOrdersId()).isEqualTo(1L),
                () -> assertThat(result.getProductId()).isEqualTo(1L),
                () -> assertThat(result.getProductName()).isEqualTo("치킨"),
                () -> assertThat(result.getProductPrice()).isEqualTo(10_000),
                () -> assertThat(result.getProductImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getOrderQuantity()).isEqualTo(10)
        );
    }

    @Test
    void findByOrderId() {
        final Long id1 = orderDetailDao.insert(OrderDetailEntityFixture.ODO1).getId();
        final Long id2 = orderDetailDao.insert(OrderDetailEntityFixture.ODO2).getId();
        final List<OrderDetailEntity> result = orderDetailDao.findByOrderId(1L);
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(id1),
                () -> assertThat(result.get(0).getOrdersId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getProductId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getProductName()).isEqualTo("치킨"),
                () -> assertThat(result.get(0).getProductPrice()).isEqualTo(10_000),
                () -> assertThat(result.get(0).getProductImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.get(0).getOrderQuantity()).isEqualTo(10),
                () -> assertThat(result.get(1).getId()).isEqualTo(id2),
                () -> assertThat(result.get(1).getOrdersId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getProductId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getProductName()).isEqualTo("피자"),
                () -> assertThat(result.get(1).getProductPrice()).isEqualTo(15_000),
                () -> assertThat(result.get(1).getProductImageUrl()).isEqualTo("http://example.com/pizza.jpg"),
                () -> assertThat(result.get(1).getOrderQuantity()).isEqualTo(10)
        );
    }
}
