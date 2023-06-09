package cart.dao;

import cart.dao.entity.OrderItemEntity;
import cart.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private OrderItemDao orderItemDao;

    @BeforeEach
    public void setUp() {

        orderItemDao = new OrderItemDao(jdbcTemplate, dataSource);
    }

    @Test
    public void createOrderItemTest() {
        OrderItemEntity orderItemEntity = new OrderItemEntity("Product1", 100L, "image_url1", 2L, 1L,1L);
        orderItemDao.createOrderItem(orderItemEntity);


   }
}
