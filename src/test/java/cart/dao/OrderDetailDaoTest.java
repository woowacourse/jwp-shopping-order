package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
}
