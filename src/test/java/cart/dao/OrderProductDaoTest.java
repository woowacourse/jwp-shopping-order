package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import anotation.RepositoryTest;
import cart.dao.dto.OrderProductDto;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderProductDaoTest {

    @Autowired
    private OrderProductDao orderProductDao;

    @Test
    @DisplayName("OrdersProduct 저장")
    void insert() {
        OrderProductDto orderProductDto = new OrderProductDto(2L, 3L, 1);

        Long orderProductId = orderProductDao.insert(orderProductDto);

        OrderProductDto orderProductDtoAfterSave = orderProductDao.findById(orderProductId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(orderProductDtoAfterSave).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderProductDto);
    }

    /**
     * INSERT INTO orders_product(order_id, product_id, quantity) VALUES (1, 1, 2);
     */
    @Test
    @DisplayName("OrdersProduct 조회하는 기능 테스트")
    void findById() {
        OrderProductDto orderProductDto = orderProductDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(orderProductDto)
                .extracting(OrderProductDto::getId, OrderProductDto::getOrderId, OrderProductDto::getProductId, OrderProductDto::getQuantity)
                .containsExactly(1L, 1L, 1L, 2);
    }

    /**
     * INSERT INTO orders_product(order_id, product_id, quantity) VALUES (1, 1, 2);
     * INSERT INTO orders_product(order_id, product_id, quantity) VALUES (1, 2, 2);
     * INSERT INTO orders_product(order_id, product_id, quantity) VALUES (1, 3, 2);
     */
    @Test
    @DisplayName("OrderId 로 OrderProduct 들을 찾는다.")
    void findOrderProductByOrderId() {
        List<OrderProductDto> orderProductDtos = orderProductDao.findByOrderId(1L);

        assertThat(orderProductDtos)
                .extracting(OrderProductDto::getOrderId, OrderProductDto::getProductId, OrderProductDto::getQuantity)
                .containsExactly(tuple(1L, 1L, 2), tuple(1L, 2L, 2), tuple(1L, 3L, 2));
    }

}