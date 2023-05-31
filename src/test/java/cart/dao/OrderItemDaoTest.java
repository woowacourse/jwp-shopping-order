package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.dto.OrderItemProductDto;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.dao.entity.ProductEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/init_cart_item.sql"})
class OrderItemDaoTest {

    private OrderDao orderDao;
    private OrderItemDao orderItemDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.orderDao = new OrderDao(jdbcTemplate);
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문 상품을 한 번에 저장할 수 있다.")
    void batchInsert() {
        // given
        OrderEntity orderEntity = new OrderEntity(1L);
        long orderId = orderDao.save(orderEntity);
        OrderItemEntity chicken = new OrderItemEntity(orderId, 1L, 3, 10_000);
        OrderItemEntity pizza = new OrderItemEntity(orderId, 2L, 2, 20_000);

        // when
        orderItemDao.batchInsert(List.of(chicken, pizza));

        // then
        assertThat(orderItemDao.findAllByOrderId(orderId)).hasSize(2);
    }

    @Test
    @DisplayName("주문번호에 따른 모든 주문 상품을 조회할 수 있다.")
    void findAllByOrderId() {
        // given
        OrderEntity orderEntity = new OrderEntity(1L);
        long orderId = orderDao.save(orderEntity);
        OrderItemEntity chicken = new OrderItemEntity(orderId, 1L, 3, 10_000);
        OrderItemEntity pizza = new OrderItemEntity(orderId, 2L, 2, 20_000);

        // when
        orderItemDao.batchInsert(List.of(chicken, pizza));

        // then
        assertThat(orderItemDao.findAllByOrderId(orderId))
            .usingRecursiveComparison()
            .comparingOnlyFields("orderItemId", "productId")
            .isEqualTo(List.of(
                new OrderItemProductDto(1L, 1L, 0, "상품명", 0, "img"),
                new OrderItemProductDto(2L, 2L, 0, "상품명", 0, "img")));
    }

    @Test
    @DisplayName("주문 당시의 금액과 현재의 상품 금액이 다르다면, 주문 당시의 금액이 조회되어야 한다.")
    void findAllByOrderId_priceAtOrderTime() {
        // given
        ProductEntity productEntity = new ProductEntity(1L, "치킨", "chickenImg", 10000);
        long productId = productEntity.getId();
        int priceAtOrderTime = productEntity.getPrice();
        long orderId = saveOrder(productId, priceAtOrderTime);

        // when
        int priceToUpdate = 20000;
        modifyProductPrice(productEntity, priceToUpdate);
        List<OrderItemProductDto> orderItems = orderItemDao.findAllByOrderId(orderId);

        // then
        assertThat(orderItems)
            .usingRecursiveComparison()
            .isEqualTo(List.of(
                new OrderItemProductDto(1L, productId, 2, productEntity.getName(), priceAtOrderTime,
                    productEntity.getImageUrl())
            ));
    }

    private long saveOrder(long productId, int priceAtOrderTime) {
        OrderEntity orderEntity = new OrderEntity(1L);
        long orderId = orderDao.save(orderEntity);
        OrderItemEntity chicken = new OrderItemEntity(orderId, productId, 2, priceAtOrderTime);
        orderItemDao.batchInsert(List.of(chicken));
        return orderId;
    }

    private void modifyProductPrice(ProductEntity entity, int price) {
        ProductDao productDao = new ProductDao(jdbcTemplate);
        productDao.updateProduct(entity.getId(),
            new ProductEntity(entity.getId(), entity.getName(), entity.getImageUrl(), price));
    }

}
