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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class CartItemDaoTest {

    private MemberEntity member;
    private ProductEntity product;
    private CartItemEntity cartItem;

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        member = new MemberEntity("a@a.com", "password1", 10);
        Long memberId = memberDao.addMember(member);
        member = member.assignId(memberId);

        product = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long productId = productDao.createProduct(product);
        product = product.assignId(productId);

        cartItem = new CartItemEntity(member, product, 1);
        Long cartItemId = cartItemDao.save(cartItem);
        cartItem = cartItem.assignId(cartItemId);
    }

    @Test
    @DisplayName("findByMemberId 메서드는 해당 멤버의 모든 장바구니 상품을 조회한다.")
    void findByMemberId() {
        MemberEntity otherMember = new MemberEntity("b@b.com", "password2", 20);
        Long otherMemberId = memberDao.addMember(otherMember);
        otherMember = otherMember.assignId(otherMemberId);

        CartItemEntity myCartItem = new CartItemEntity(member, product, 1);
        CartItemEntity otherCartItem = new CartItemEntity(otherMember, product, 1);
        Long myCartItemId = cartItemDao.save(myCartItem);
        cartItemDao.save(otherCartItem);

        List<CartItemEntity> result = cartItemDao.findByMemberId(member.getId());

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0)).usingRecursiveComparison()
                        .ignoringFieldsOfTypes(LocalDateTime.class)
                        .isEqualTo(cartItem),
                () -> assertThat(result.get(0).getCreatedAt()).isNotNull(),
                () -> assertThat(result.get(0).getUpdatedAt()).isNotNull(),
                () -> assertThat(result.get(1)).usingRecursiveComparison()
                        .ignoringFieldsOfTypes(LocalDateTime.class)
                        .isEqualTo(myCartItem.assignId(myCartItemId)),
                () -> assertThat(result.get(1).getCreatedAt()).isNotNull(),
                () -> assertThat(result.get(1).getUpdatedAt()).isNotNull()
        );
    }

    @Nested
    @DisplayName("findByMemberIdAndProductId 메서드는 ")
    class FindByMemberIdAndProductId {

        @Test
        @DisplayName("멤버 ID는 일치하지 않고, 상품 ID는 일치하지 않으면 빈 값을 반환한다.")
        void notMatchMemberAndProduct() {
            Optional<CartItemEntity> result = cartItemDao.findByMemberIdAndProductId(-1L, -1L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("멤버 ID는 일치하지 않고, 상품 ID는 일치한다면 빈 값을 반환한다.")
        void notMatchMember() {
            Optional<CartItemEntity> result = cartItemDao.findByMemberIdAndProductId(-1L, product.getId());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("멤버 ID는 일치하고, 상품 ID는 일치하지 않으면 빈 값을 반환한다.")
        void notMatchProduct() {
            Optional<CartItemEntity> result = cartItemDao.findByMemberIdAndProductId(member.getId(), -1L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("멤버 ID, 상품 ID가 일치한다면 장바구니 상품을 조회한다.")
        void matchMemberAndProduct() {
            Optional<CartItemEntity> result = cartItemDao.findByMemberIdAndProductId(member.getId(), product.getId());

            assertAll(
                    () -> assertThat(result).isNotEmpty(),
                    () -> assertThat(result.get()).usingRecursiveComparison()
                            .ignoringFieldsOfTypes(LocalDateTime.class)
                            .isEqualTo(cartItem),
                    () -> assertThat(result.get().getCreatedAt()).isNotNull(),
                    () -> assertThat(result.get().getUpdatedAt()).isNotNull()
            );
        }
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
            Optional<CartItemEntity> result = cartItemDao.findById(cartItem.getId());

            assertAll(
                    () -> assertThat(result).isNotEmpty(),
                    () -> assertThat(result.get()).usingRecursiveComparison()
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
        CartItemEntity updateCartItem = new CartItemEntity(cartItem.getId(), member, product, 5);

        cartItemDao.updateQuantity(updateCartItem);

        Optional<CartItemEntity> result = cartItemDao.findById(cartItem.getId());
        assertAll(
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result.get().getQuantity()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("deleteById 메서드는 장바구니 상품을 삭제한다.")
    void deleteById() {
        cartItemDao.deleteById(cartItem.getId());

        Optional<CartItemEntity> result = cartItemDao.findById(cartItem.getId());
        assertThat(result).isEmpty();
    }
}
