package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.dto.order.OrderItemProductDto;
import cart.dao.dto.order.OrderDto;
import cart.dao.dto.order.OrderItemDto;
import cart.dao.dto.product.ProductDto;
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
        OrderDto orderDto = new OrderDto(1L);
        long orderId = orderDao.save(orderDto);
        OrderItemDto chicken = new OrderItemDto(orderId, 1L, 3, 10_000);
        OrderItemDto pizza = new OrderItemDto(orderId, 2L, 2, 20_000);

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
            new ProductDto(1L, "치킨", "chickenImg", 10000),
            new ProductDto(3L, "피자", "pizzaImg", 13000));

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
        ProductDto productDto = new ProductDto(1L, "치킨", "chickenImg", 10000);
        long productId = productDto.getId();
        int priceAtOrderTime = productDto.getPrice();
        long orderId = saveOrder(productDto);

        // when
        int priceToUpdate = 20000;
        modifyProductPrice(productDto, priceToUpdate);
        List<OrderItemProductDto> orderItems = orderItemDao.findAllByOrderId(orderId);

        // then
        assertThat(orderItems)
            .usingRecursiveComparison()
            .isEqualTo(List.of(
                new OrderItemProductDto(orderId, 1L, productId, 2, productDto.getName(), priceAtOrderTime,
                    productDto.getImageUrl())
            ));
    }

    private long saveOrder(ProductDto... productEntities) {
        OrderDto orderDto = new OrderDto(1L);
        long orderId = orderDao.save(orderDto);

        List<OrderItemDto> orderItemsToSave = Arrays.stream(productEntities)
            .map(product -> new OrderItemDto(orderId, product.getId(), 2, product.getPrice()))
            .collect(Collectors.toList());
        orderItemDao.batchInsert(orderItemsToSave);
        return orderId;
    }

    private void modifyProductPrice(ProductDto entity, int price) {
        ProductDao productDao = new ProductDao(jdbcTemplate);
        productDao.updateProduct(entity.getId(),
            new ProductDto(entity.getId(), entity.getName(), entity.getImageUrl(), price));
    }

    @Test
    @DisplayName("주문 id 여러 개에 대한 모든 주문 상품을 조회할 수 있다.")
    void findAllByOrderIds() {
        // given
        long orderId1 = saveOrder(
            new ProductDto(1L, "치킨", "chickenImg", 10000),
            new ProductDto(3L, "피자", "pizzaImg", 13000));
        long orderId2 = saveOrder(
            new ProductDto(1L, "치킨", "chickenImg", 10000));

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
