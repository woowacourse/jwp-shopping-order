package cart.dao;

import cart.dao.order.DbOrderDao;
import cart.dao.order.OrderDao;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
class DbOrderDaoTest {

    @Autowired
    private DataSource dataSource;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new DbOrderDao(dataSource);
    }

    @Test
    @DisplayName("")
    void insert() {
        OrderEntity orderEntity = new OrderEntity(
                null,
                1L,
                new Timestamp(System.currentTimeMillis()),
                3000,
                2000);
        Long id = orderDao.insert(orderEntity);
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("")
    void findById() {
        OrderEntity orderEntity = new OrderEntity(
                null,
                1L,
                new Timestamp(System.currentTimeMillis()),
                3000,
                2000);
        Long id = orderDao.insert(orderEntity);

        assertThat(orderDao.findById(id))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderEntity);
    }
}