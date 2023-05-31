package cart.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
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

class OrderMapperTest {

    @Test
    @DisplayName("toDomain 메서드는 엔티티를 도메인으로 변환한다.")
    void convertFromEntityToDomain() {
        MemberEntity memberEntity = new MemberEntity(1L, "a@a.com", "password1", 10);
        OrderEntity orderEntity = new OrderEntity(1L, memberEntity, 0, 0);
        ProductEntity productEntityA = new ProductEntity(1L, "치킨", 10000, "http://chicken.com");
        ProductEntity productEntityB = new ProductEntity(2L, "피자", 20000, "http://pizza.com");
        OrderProductEntity orderProductEntityA =
                new OrderProductEntity(1L, productEntityA, "치킨", 10000, "http://chicken.com", 5);
        OrderProductEntity orderProductEntityB =
                new OrderProductEntity(2L, productEntityB, "피자", 20000, "http://pizza.com", 5);

        Order result = OrderMapper.toDomain(orderEntity, List.of(orderProductEntityA, orderProductEntityB));

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(orderEntity.getId()),
                () -> assertThat(result.getMember()).usingRecursiveComparison().isEqualTo(MemberMapper.toDomain(memberEntity)),
                () -> assertThat(result.getOrderProducts()).usingRecursiveComparison()
                        .isEqualTo(
                                List.of(
                                        OrderProductMapper.toDomain(orderProductEntityA),
                                        OrderProductMapper.toDomain(orderProductEntityB)
                                )
                        ),
                () -> assertThat(result.getUsedPoint()).isEqualTo(orderEntity.getUsedPoint())
        );
    }

    @Test
    @DisplayName("toEntity 메서드는 도메인을 엔티티로 변환한다.")
    void convertFromDomainToEntity() {
        Member member = new Member(1L, "a@a.com", "password1", 10);
        Product productA = new Product(1L, "치킨", 10000, "http://chicken.com");
        Product productB = new Product(2L, "피자", 20000, "http://pizza.com");
        OrderProduct orderProductA = new OrderProduct(1L, productA, 10);
        OrderProduct orderProductB = new OrderProduct(2L, productB, 10);
        Order order = new Order(1L, member, List.of(orderProductA, orderProductB), 10, LocalDateTime.now());

        OrderEntity result = OrderMapper.toEntity(order);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(order.getId()),
                () -> assertThat(result.getMemberEntity()).isEqualTo(MemberMapper.toEntity(order.getMember())),
                () -> assertThat(result.getUsedPoint()).isEqualTo(order.getUsedPoint())
        );
    }
}
