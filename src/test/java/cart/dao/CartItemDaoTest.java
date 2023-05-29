package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.CartItemEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import cart.test.RepositoryTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class CartItemDaoTest {

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("findByMemberId 메서드는 해당 멤버의 모든 장바구니 상품을 조회한다.")
    void findByMemberId() {
        MemberEntity memberA = new MemberEntity("a@a.com", "password1", 10);
        MemberEntity memberB = new MemberEntity("b@b.com", "password2", 20);
        ProductEntity product = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long memberIdA = memberDao.addMember(memberA);
        Long memberIdB = memberDao.addMember(memberB);
        Long productId = productDao.createProduct(product);
        CartItemEntity cartItemA = new CartItemEntity(
                new MemberEntity(memberIdA, memberA.getEmail(), memberA.getPassword(), memberA.getPoint()),
                new ProductEntity(productId, product.getName(), product.getPrice(), product.getImageUrl()),
                1
        );
        CartItemEntity cartItemB = new CartItemEntity(
                new MemberEntity(memberIdA, memberA.getEmail(), memberA.getPassword(), memberA.getPoint()),
                new ProductEntity(productId, product.getName(), product.getPrice(), product.getImageUrl()),
                1
        );
        CartItemEntity cartItemC = new CartItemEntity(
                new MemberEntity(memberIdB, memberB.getEmail(), memberB.getPassword(), memberB.getPoint()),
                new ProductEntity(productId, product.getName(), product.getPrice(), product.getImageUrl()),
                1
        );
        Long cartItemIdAForMemberA = cartItemDao.save(cartItemA);
        Long cartItemIdBForMemberA = cartItemDao.save(cartItemB);
        cartItemDao.save(cartItemC);

        List<CartItemEntity> result = cartItemDao.findByMemberId(memberIdA);

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(cartItemIdAForMemberA),
                () -> assertThat(result.get(0)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .ignoringFieldsOfTypes(LocalDateTime.class)
                        .isEqualTo(cartItemA),
                () -> assertThat(result.get(0).getCreatedAt()).isNotNull(),
                () -> assertThat(result.get(0).getUpdatedAt()).isNotNull(),
                () -> assertThat(result.get(1).getId()).isEqualTo(cartItemIdBForMemberA),
                () -> assertThat(result.get(1)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .ignoringFieldsOfTypes(LocalDateTime.class)
                        .isEqualTo(cartItemB),
                () -> assertThat(result.get(1).getCreatedAt()).isNotNull(),
                () -> assertThat(result.get(1).getUpdatedAt()).isNotNull()
        );
    }

    @Nested
    @DisplayName("findById 메서드는 ")
    class FindById {

        @Test
        @DisplayName("ID에 해당하는 장바구니 상품이 존재하지 않으면 빈 값을 반환한다.")
        void emptyCartItem() {
            Optional<CartItemEntity> result = cartItemDao.findById(-1L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("ID에 해당하는 장바구니 상품이 존재하면 장바구니 상품을 조회한다.")
        void getCartItem() {
            MemberEntity member = new MemberEntity("a@a.com", "password1", 10);
            ProductEntity product = new ProductEntity("치킨", 10000, "http://chicken.com");
            Long memberId = memberDao.addMember(member);
            Long productId = productDao.createProduct(product);
            CartItemEntity cartItem = new CartItemEntity(
                    new MemberEntity(memberId, member.getEmail(), member.getPassword(), member.getPoint()),
                    new ProductEntity(productId, product.getName(), product.getPrice(), product.getImageUrl()),
                    1
            );
            Long cartItemId = cartItemDao.save(cartItem);

            Optional<CartItemEntity> result = cartItemDao.findById(cartItemId);

            assertAll(
                    () -> assertThat(result).isNotEmpty(),
                    () -> assertThat(result.get().getId()).isEqualTo(cartItemId),
                    () -> assertThat(result.get()).usingRecursiveComparison()
                            .ignoringFields("id")
                            .ignoringFieldsOfTypes(LocalDateTime.class)
                            .isEqualTo(cartItem),
                    () -> assertThat(result.get().getCreatedAt()).isNotNull(),
                    () -> assertThat(result.get().getUpdatedAt()).isNotNull()
            );
        }
    }

    @Test
    @DisplayName("updateQuantity 메서드는 장바구니 상품 수량을 수정한다.")
    void updateQuantity() {
        MemberEntity member = new MemberEntity("a@a.com", "password1", 10);
        ProductEntity product = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long memberId = memberDao.addMember(member);
        Long productId = productDao.createProduct(product);
        CartItemEntity cartItem = new CartItemEntity(
                new MemberEntity(memberId, member.getEmail(), member.getPassword(), member.getPoint()),
                new ProductEntity(productId, product.getName(), product.getPrice(), product.getImageUrl()),
                1
        );
        Long cartItemId = cartItemDao.save(cartItem);
        CartItemEntity updateCartItem = new CartItemEntity(
                cartItemId,
                new MemberEntity(memberId, member.getEmail(), member.getPassword(), member.getPoint()),
                new ProductEntity(productId, product.getName(), product.getPrice(), product.getImageUrl()),
                5
        );

        cartItemDao.updateQuantity(updateCartItem);

        Optional<CartItemEntity> result = cartItemDao.findById(cartItemId);

        assertAll(
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result.get().getQuantity()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("deleteById 메서드는 장바구니 상품을 삭제한다.")
    void deleteById() {
        MemberEntity member = new MemberEntity("a@a.com", "password1", 10);
        ProductEntity product = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long memberId = memberDao.addMember(member);
        Long productId = productDao.createProduct(product);
        CartItemEntity cartItem = new CartItemEntity(
                new MemberEntity(memberId, member.getEmail(), member.getPassword(), member.getPoint()),
                new ProductEntity(productId, product.getName(), product.getPrice(), product.getImageUrl()),
                1
        );
        Long cartItemId = cartItemDao.save(cartItem);

        cartItemDao.deleteById(cartItemId);

        Optional<CartItemEntity> result = cartItemDao.findById(cartItemId);
        assertThat(result).isEmpty();
    }
}
