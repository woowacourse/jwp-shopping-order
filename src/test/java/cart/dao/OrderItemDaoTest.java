package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.dto.OrderItemProductDto;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.dao.entity.ProductEntity;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
        long orderId = saveOrder(
            new ProductEntity(1L, "치킨", "chickenImg", 10000),
            new ProductEntity(3L, "피자", "pizzaImg", 13000));

        // when, then
        assertThat(orderItemDao.findAllByOrderId(orderId))
            .usingRecursiveComparison()
            .isEqualTo(List.of(
                new OrderItemProductDto(orderId, 1L, 1L, 2, "치킨", 10000, "chickenImg"),
                new OrderItemProductDto(orderId, 2L, 3L, 2, "피자", 13000, "pizzaImg")));
    }

    @Test
    @DisplayName("주문 당시의 금액과 현재의 상품 금액이 다르다면, 주문 당시의 금액이 조회되어야 한다.")
    void findAllByOrderId_priceAtOrderTime() {
        // given
        ProductEntity productEntity = new ProductEntity(1L, "치킨", "chickenImg", 10000);
        long productId = productEntity.getId();
        int priceAtOrderTime = productEntity.getPrice();
        long orderId = saveOrder(productEntity);

        // when
        int priceToUpdate = 20000;
        modifyProductPrice(productEntity, priceToUpdate);
        List<OrderItemProductDto> orderItems = orderItemDao.findAllByOrderId(orderId);

        // then
        assertThat(orderItems)
            .usingRecursiveComparison()
            .isEqualTo(List.of(
                new OrderItemProductDto(orderId, 1L, productId, 2, productEntity.getName(), priceAtOrderTime,
                    productEntity.getImageUrl())
            ));
    }

    private long saveOrder(ProductEntity... productEntities) {
        OrderEntity orderEntity = new OrderEntity(1L);
        long orderId = orderDao.save(orderEntity);

        List<OrderItemEntity> orderItemsToSave = Arrays.stream(productEntities)
            .map(product -> new OrderItemEntity(orderId, product.getId(), 2, product.getPrice()))
            .collect(Collectors.toList());
        orderItemDao.batchInsert(orderItemsToSave);
        return orderId;
    }

    private void modifyProductPrice(ProductEntity entity, int price) {
        ProductDao productDao = new ProductDao(jdbcTemplate);
        productDao.updateProduct(entity.getId(),
            new ProductEntity(entity.getId(), entity.getName(), entity.getImageUrl(), price));
    }

    @Test
    @DisplayName("주문 id 여러 개에 대한 모든 주문 상품을 조회할 수 있다.")
    void findAllByOrderIds() {
        // given
        long orderId1 = saveOrder(
            new ProductEntity(1L, "치킨", "chickenImg", 10000),
            new ProductEntity(3L, "피자", "pizzaImg", 13000));
        long orderId2 = saveOrder(
            new ProductEntity(1L, "치킨", "chickenImg", 10000));

        // when
        List<OrderItemProductDto> orderItemProducts = orderItemDao.findAllByOrderIds(
            List.of(orderId1, orderId2));

        // then
        assertThat(orderItemProducts)
            .usingRecursiveComparison()
            .isEqualTo(List.of(
                new OrderItemProductDto(orderId1, 1L, 1L, 2, "치킨", 10000, "chickenImg"),
                new OrderItemProductDto(orderId1, 2L, 3L, 2, "피자", 13000, "pizzaImg"),
                new OrderItemProductDto(orderId2, 3L, 1L, 2, "치킨", 10000, "chickenImg")
            ));
    }

}
