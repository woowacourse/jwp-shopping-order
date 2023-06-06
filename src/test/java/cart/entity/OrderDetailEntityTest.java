package cart.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class OrderDetailEntityTest {

    @Test
    void constructWithBuilder() {
        final OrderDetailEntity result = OrderDetailEntity.builder()
                .id(1L)
                .ordersId(2L)
                .productId(3L)
                .productName("치킨")
                .productPrice(10_000)
                .productImageUrl("http://example.com/chicken.jpg")
                .orderQuantity(10)
                .build();
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getOrdersId()).isEqualTo(2L),
                () -> assertThat(result.getProductId()).isEqualTo(3L),
                () -> assertThat(result.getProductName()).isEqualTo("치킨"),
                () -> assertThat(result.getProductPrice()).isEqualTo(10_000),
                () -> assertThat(result.getProductImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getOrderQuantity()).isEqualTo(10)
        );
    }

    @Test
    void constructWithOther() {
        final OrderDetailEntity orderDetailEntity = OrderDetailEntity.builder()
                .id(1L)
                .ordersId(2L)
                .productId(3L)
                .productName("치킨")
                .productPrice(10_000)
                .productImageUrl("http://example.com/chicken.jpg")
                .orderQuantity(10)
                .build();
        final OrderDetailEntity result = new OrderDetailEntity(10L, orderDetailEntity);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(10L),
                () -> assertThat(result.getOrdersId()).isEqualTo(2L),
                () -> assertThat(result.getProductId()).isEqualTo(3L),
                () -> assertThat(result.getProductName()).isEqualTo("치킨"),
                () -> assertThat(result.getProductPrice()).isEqualTo(10_000),
                () -> assertThat(result.getProductImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getOrderQuantity()).isEqualTo(10)
        );
    }
}
