package cart.dao;

import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private OrderDao orderDao;

    @BeforeEach
    public void setUp() {
        orderDao = new OrderDao(jdbcTemplate, dataSource);
    }

    @Test
    public void createOrderItemTest() {
        OrderEntity orderEntity = new OrderEntity(1L,0L,10000L,1L);
        orderDao.createOrder(orderEntity);
    }

}
