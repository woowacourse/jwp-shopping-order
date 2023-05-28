package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import cart.dao.dto.OrderProductDto;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@JdbcTest
class OrderProductDaoTest {

    private static final RowMapper<OrderProductDto> orderProductDtoRowMapper = (rs, rn) -> new OrderProductDto(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity")
    );
    private static final String INSERT_QUERY =
            "INSERT INTO orders_product(id, order_id, product_id, quantity) VALUES (?, ?, ?, ?)";
    private static final String SELECT_QUERY =
            "Select * from orders_product where id = ?";

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private OrderProductDao orderProductDao;

    @BeforeEach
    void beforeEach() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        orderProductDao = new OrderProductDao(dataSource);
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
    }

    @Test
    @DisplayName("OrdersProduct 저장")
    void insert() {
        final OrderProductDto orderProductDto = new OrderProductDto(2L, 3L, 1);

        final Long orderProductId = orderProductDao.insert(orderProductDto);

        final OrderProductDto queryResultOrderProduct
                = jdbcTemplate.queryForObject(SELECT_QUERY, orderProductDtoRowMapper, orderProductId);
        assertThat(queryResultOrderProduct)
                .extracting(
                        OrderProductDto::getId, OrderProductDto::getOrderId,
                        OrderProductDto::getProductId, OrderProductDto::getQuantity
                )
                .containsExactly(orderProductId, orderProductDto.getOrderId()
                        , orderProductDto.getProductId(), orderProductDto.getQuantity());
    }

    @Test
    @DisplayName("id로 OrdersProduct 조회하는 기능 테스트")
    void findById() {
        final OrderProductDto orderProduct = new OrderProductDto(1L, 2L, 3L, 4);
        initOrderProduct(orderProduct);

        final OrderProductDto queryResultOrderProduct = orderProductDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(queryResultOrderProduct)
                .extracting(
                        OrderProductDto::getId, OrderProductDto::getOrderId,
                        OrderProductDto::getProductId, OrderProductDto::getQuantity
                )
                .containsExactly(orderProduct.getId(), orderProduct.getOrderId()
                        , orderProduct.getProductId(), orderProduct.getQuantity());
    }

    @Test
    @DisplayName("orderId로 OrderProduct들을 조회하는 기능 테스트")
    void findByOrderId() {
        final Long orderId = 2L;
        final OrderProductDto orderProduct1 = new OrderProductDto(1L, orderId, 3L, 4);
        final OrderProductDto orderProduct2 = new OrderProductDto(2L, orderId, 4L, 4);
        initOrderProduct(orderProduct1);
        initOrderProduct(orderProduct2);

        final List<OrderProductDto> orderProducts = orderProductDao.findByOrderId(orderId);

        assertThat(orderProducts)
                .extracting(
                        OrderProductDto::getId, OrderProductDto::getOrderId,
                        OrderProductDto::getProductId, OrderProductDto::getQuantity
                )
                .containsExactlyInAnyOrder(
                        tuple(orderProduct1.getId(), orderProduct1.getOrderId()
                                , orderProduct1.getProductId(), orderProduct1.getQuantity()),
                        tuple(orderProduct2.getId(), orderProduct2.getOrderId()
                                , orderProduct2.getProductId(), orderProduct2.getQuantity())
                );
    }

    private void initOrderProduct(final OrderProductDto orderProduct1) {
        jdbcTemplate.update(INSERT_QUERY
                , orderProduct1.getId(), orderProduct1.getOrderId()
                , orderProduct1.getProductId(), orderProduct1.getQuantity()
        );
    }
}
