package cart.dao;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.entity.OrderEntity;
import cart.entity.OrderProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class OrderDaoTest {

    @Autowired
    DataSource dataSource;

    OrderDao orderDao;

    @BeforeEach
    void setting() {
        orderDao = new OrderDao(new JdbcTemplate(dataSource));
    }

    @DisplayName("주문을 저장한다.")
    @Test
    void save() throws SQLException {
        //given
        final Member member = new Member(1L, "email", "password");
        final Order order = new Order(List.of(
                new OrderProduct(new Product(1L, "productA", 1000, "image1"), 3),
                new OrderProduct(new Product(2L, "productB", 10000, "image2"), 5)
        ));

        //when
        final long savedOrder = orderDao.save(member, order);
        final OrderEntity orderEntity = orderDao.findById(savedOrder);
        final List<OrderProductEntity> orderProductEntities = orderDao.findByOrderId(savedOrder);

        //then
        assertAll(
                () -> assertThat(orderEntity.getMemberId()).isEqualTo(1L),
                () -> assertThat(orderEntity.getPrice()).isEqualTo(53000),
                () -> assertThat(orderProductEntities.get(0).getOrderId()).isEqualTo(savedOrder),
                () -> assertThat(orderProductEntities.get(0).getProductId()).isEqualTo(1L),
                () -> assertThat(orderProductEntities.get(0).getQuantity()).isEqualTo(3),
                () -> assertThat(orderProductEntities.get(1).getOrderId()).isEqualTo(savedOrder),
                () -> assertThat(orderProductEntities.get(1).getProductId()).isEqualTo(2L),
                () -> assertThat(orderProductEntities.get(1).getQuantity()).isEqualTo(5)
        );
    }
}
