package cart.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.Product;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderProductMapperTest {

    @Test
    @DisplayName("toDomain 메서드는 엔티티를 도메인으로 변환한다.")
    void convertFromEntityToDomain() {
        ProductEntity productEntity = new ProductEntity(1L, "치킨", 10000, "http://chicken.com");
        OrderProductEntity orderProductEntity =
                new OrderProductEntity(1L, 1L, productEntity, "치킨", 10000, "http://chicken.com", 10);

        OrderProduct result = OrderProductMapper.toDomain(orderProductEntity);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(orderProductEntity.getId()),
                () -> assertThat(result.getProduct()).usingRecursiveComparison().isEqualTo(ProductMapper.toDomain(productEntity)),
                () -> assertThat(result.getQuantity()).isEqualTo(orderProductEntity.getQuantity())
        );
    }

    @Test
    @DisplayName("toEntity 메서드는 도메인을 엔티티로 변환한다.")
    void convertFromDomainToEntity() {
        Member member = new Member(1L, "a@a.com", "password1", 10);
        Product product = new Product(1L, "치킨", 10000, "http://chicken.com");
        OrderProduct orderProduct = new OrderProduct(1L, product, 10);
        Order order = new Order(1L, member, List.of(orderProduct), 0, LocalDateTime.now());

        OrderProductEntity result = OrderProductMapper.toEntity(order, orderProduct);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(orderProduct.getId()),
                () -> assertThat(result.getOrderId()).isEqualTo(order.getId()),
                () -> assertThat(result.getProductName()).isEqualTo(product.getName()),
                () -> assertThat(result.getProductPrice()).isEqualTo(product.getPrice()),
                () -> assertThat(result.getProductImageUrl()).isEqualTo(product.getImageUrl()),
                () -> assertThat(result.getQuantity()).isEqualTo(orderProduct.getQuantity())
        );
    }
}
