package cart.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.CartItemEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartItemMapperTest {

    @Test
    @DisplayName("엔티티를 도메인으로 변환한다.")
    void convertFromEntityToDomain() {
        CartItemEntity entity = new CartItemEntity(
                1L,
                new MemberEntity(1L, "a@a.com", "password1", 10),
                new ProductEntity(1L, "치킨", 13000, "http://chicken.com"),
                10
        );

        CartItem result = CartItemMapper.toDomain(entity);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(entity.getId()),
                () -> assertThat(result.getMember()).usingRecursiveComparison()
                        .isEqualTo(MemberMapper.toDomain(entity.getMemberEntity())),
                () -> assertThat(result.getProduct()).usingRecursiveComparison()
                        .isEqualTo(ProductMapper.toDomain(entity.getProductEntity())),
                () -> assertThat(result.getQuantity()).isEqualTo(entity.getQuantity())
        );
    }

    @Test
    @DisplayName("도메인을 엔티티로 변환한다.")
    void convertFromDomainToEntity() {
        CartItem domain = new CartItem(
                1L,
                10,
                new Product(1L, "치킨", 13000, "http://chicken.com"),
                new Member(1L, "a@a.com", "password1", 0)
        );

        CartItemEntity result = CartItemMapper.toEntity(domain);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(domain.getId()),
                () -> assertThat(result.getMemberEntity()).usingRecursiveComparison()
                        .isEqualTo(MemberMapper.toEntity(domain.getMember())),
                () -> assertThat(result.getProductEntity()).usingRecursiveComparison()
                        .isEqualTo(ProductMapper.toEntity(domain.getProduct())),
                () -> assertThat(result.getQuantity()).isEqualTo(domain.getQuantity())
        );
    }
}
