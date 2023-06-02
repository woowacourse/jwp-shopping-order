package cart.dao;

import cart.entity.OrderEntity;
import cart.entity.OrderProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class OrderProductDaoTest {

    private OrderProductDao orderProductDao;
    private OrderDao orderDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        orderProductDao = new OrderProductDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void saveOrderProductsByOrderId() {
        Long orderId = orderDao.saveOrder(new OrderEntity(1L, 1000, 1000));
        OrderProductEntity orderProduct = new OrderProductEntity("오션", "ocean.com", 10000, 10, orderId);
        List<OrderProductEntity> orderProducts = List.of(orderProduct);

        orderProductDao.saveOrderProductsByOrderId(orderId, orderProducts);
        OrderProductEntity findOrderProduct = orderProductDao.findAllByOrderId(orderId).get(0);

        assertAll(
                () -> assertThat(findOrderProduct.getName()).isEqualTo("오션"),
                () -> assertThat(findOrderProduct.getImage_url()).isEqualTo("ocean.com"),
                () -> assertThat(findOrderProduct.getQuantity()).isEqualTo(10),
                () -> assertThat(findOrderProduct.getPrice()).isEqualTo(10000)
        );
    }
}
