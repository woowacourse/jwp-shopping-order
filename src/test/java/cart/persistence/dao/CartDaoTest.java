package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.dto.CartItemDto;
import cart.persistence.entity.CartEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import({CartItemDao.class, MemberDao.class, ProductDao.class})
class CartDaoTest extends DaoTest {

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    private MemberEntity 멤버_져니;
    private ProductEntity 치킨, 피자;

    @BeforeEach
    void setUp() {
        멤버_져니 = new MemberEntity("journey", "password");
        치킨 = new ProductEntity("치킨", "chicken_image_url", 20000);
        피자 = new ProductEntity("피자", "pizza_image_url", 30000);
    }

    @Test
    @DisplayName("장바구니 정보를 저장한다.")
    void insert() {
        // given
        final long 져장된_져니_아이디 = memberDao.insert(멤버_져니);
        final long 저장된_치킨_아이디 = productDao.insert(치킨);
        final CartEntity 장바구니 = new CartEntity(져장된_져니_아이디, 저장된_치킨_아이디, 1);

        // when
        final Long 저장된_장바구니_아이디 = cartItemDao.insert(장바구니);

        // then
        final CartItemDto cart = cartItemDao.findById(저장된_장바구니_아이디);
        assertThat(cart)
            .extracting(CartItemDto::getCartId, CartItemDto::getMemberId, CartItemDto::getMemberName,
                CartItemDto::getProductId, CartItemDto::getProductName, CartItemDto::getProductPrice,
                CartItemDto::getProductImageUrl, CartItemDto::getProductQuantity)
            .containsExactly(저장된_장바구니_아이디, 져장된_져니_아이디, "journey", 저장된_치킨_아이디,
                "치킨", 20000, "chicken_image_url", 1);
    }

    @Test
    @DisplayName("사용자의 장바구니 정보를 조회한다.")
    void findByMemberName() {
        // given
        final long 저장된_져니_아이디 = memberDao.insert(멤버_져니);
        final long 저장된_치킨_아이디 = productDao.insert(치킨);
        final Long 저장된_피자_아이디 = productDao.insert(new ProductEntity("피자", "pizza_image_url", 30000));
        final CartEntity 장바구니_치킨 = new CartEntity(저장된_져니_아이디, 저장된_치킨_아이디, 1);
        final CartEntity 장바구니_피자 = new CartEntity(저장된_져니_아이디, 저장된_피자_아이디, 10);
        final Long 저장된_장바구니_치킨_아이디 = cartItemDao.insert(장바구니_치킨);
        final Long 저장된_장바구니_피자_아이디 = cartItemDao.insert(장바구니_피자);

        // when
        final List<CartItemDto> cart = cartItemDao.findByMemberName("journey");

        // then
        assertThat(cart).hasSize(2);
        assertThat(cart)
            .extracting(CartItemDto::getCartId, CartItemDto::getMemberId, CartItemDto::getMemberName,
                CartItemDto::getProductId, CartItemDto::getProductName, CartItemDto::getProductPrice,
                CartItemDto::getProductImageUrl, CartItemDto::getProductQuantity)
            .containsExactly(
                tuple(저장된_장바구니_치킨_아이디, 저장된_져니_아이디, "journey", 저장된_치킨_아이디, "치킨", 20000, "chicken_image_url", 1),
                tuple(저장된_장바구니_피자_아이디, 저장된_져니_아이디, "journey", 저장된_피자_아이디, "피자", 30000, "pizza_image_url", 10)
            );
    }

    @Test
    @DisplayName("유효한 장바구니 아이디로 장바구니 정보를 조회한다.")
    void findById() {
        // given
        final long 져장된_져니_아이디 = memberDao.insert(멤버_져니);
        final long 저장된_치킨_아이디 = productDao.insert(치킨);
        final CartEntity 장바구니 = new CartEntity(져장된_져니_아이디, 저장된_치킨_아이디, 1);
        final Long 저장된_장바구니_아이디 = cartItemDao.insert(장바구니);

        // when
        final CartItemDto cart = cartItemDao.findById(저장된_장바구니_아이디);

        // then
        assertThat(cart)
            .extracting(CartItemDto::getCartId, CartItemDto::getMemberId, CartItemDto::getMemberName,
                CartItemDto::getProductId, CartItemDto::getProductName, CartItemDto::getProductPrice,
                CartItemDto::getProductImageUrl, CartItemDto::getProductQuantity)
            .containsExactly(저장된_장바구니_아이디, 져장된_져니_아이디, "journey", 저장된_치킨_아이디,
                "치킨", 20000, "chicken_image_url", 1);
    }

    @Test
    @DisplayName("장바구니의 수량 정보를 업데이트한다.")
    void updateQuantity() {
        // given
        final long 져장된_져니_아이디 = memberDao.insert(멤버_져니);
        final long 저장된_치킨_아이디 = productDao.insert(치킨);
        final CartEntity 장바구니 = new CartEntity(져장된_져니_아이디, 저장된_치킨_아이디, 1);
        final Long 저장된_장바구니_아이디 = cartItemDao.insert(장바구니);

        // when
        cartItemDao.updateQuantity(저장된_장바구니_아이디, 10);

        // then
        final CartItemDto cart = cartItemDao.findById(저장된_장바구니_아이디);

        // then
        assertThat(cart)
            .extracting(CartItemDto::getCartId, CartItemDto::getMemberId, CartItemDto::getMemberName,
                CartItemDto::getProductId, CartItemDto::getProductName, CartItemDto::getProductPrice,
                CartItemDto::getProductImageUrl, CartItemDto::getProductQuantity)
            .containsExactly(저장된_장바구니_아이디, 져장된_져니_아이디, "journey", 저장된_치킨_아이디,
                "치킨", 20000, "chicken_image_url", 10);
    }

    @Test
    @DisplayName("장바구니의 정보를 삭제한다.")
    void deleteById() {
        // given
        final long 져장된_져니_아이디 = memberDao.insert(멤버_져니);
        final long 저장된_치킨_아이디 = productDao.insert(치킨);
        final CartEntity 장바구니 = new CartEntity(져장된_져니_아이디, 저장된_치킨_아이디, 1);
        final Long 저장된_장바구니_아이디 = cartItemDao.insert(장바구니);

        // when
        final int deletedCount = cartItemDao.deleteById(저장된_장바구니_아이디);

        // then
        assertThat(deletedCount).isSameAs(1);
    }

    @Test
    @DisplayName("사용자의 아이디와 장바구니 상품 아이디로 장바구니 상품 개수를 조회한다.")
    void countByIdsAndMemberId() {
        // given
        final long 저장된_져니_아이디 = memberDao.insert(멤버_져니);
        final long 저장된_치킨_아이디 = productDao.insert(치킨);
        final Long 저장된_피자_아이디 = productDao.insert(피자);
        final CartEntity 장바구니_치킨 = new CartEntity(저장된_져니_아이디, 저장된_치킨_아이디, 1);
        final CartEntity 장바구니_피자 = new CartEntity(저장된_져니_아이디, 저장된_피자_아이디, 10);
        final Long 저장된_장바구니_치킨_아이디 = cartItemDao.insert(장바구니_치킨);
        final Long 저장된_장바구니_피자_아이디 = cartItemDao.insert(장바구니_피자);

        // when
        final Long cartItemCount = cartItemDao.countByIdsAndMemberId(List.of(저장된_장바구니_치킨_아이디, 저장된_장바구니_피자_아이디),
            저장된_져니_아이디);

        // then
        assertThat(cartItemCount).isSameAs(2L);
    }

    @Test
    @DisplayName("사용자의 아이디와 장바구니 상품 아이디로 장바구니 상품을 제거한다.")
    void deleteByIdsAndMemberId() {
        // given
        final long 저장된_져니_아이디 = memberDao.insert(멤버_져니);
        final long 저장된_치킨_아이디 = productDao.insert(치킨);
        final Long 저장된_피자_아이디 = productDao.insert(new ProductEntity("피자", "pizza_image_url", 30000));
        final CartEntity 장바구니_치킨 = new CartEntity(저장된_져니_아이디, 저장된_치킨_아이디, 1);
        final CartEntity 장바구니_피자 = new CartEntity(저장된_져니_아이디, 저장된_피자_아이디, 10);
        final Long 저장된_장바구니_치킨_아이디 = cartItemDao.insert(장바구니_치킨);
        final Long 저장된_장바구니_피자_아이디 = cartItemDao.insert(장바구니_피자);

        // when
        final int deletedCount = cartItemDao.deleteByIdsAndMemberId(List.of(저장된_장바구니_치킨_아이디, 저장된_장바구니_피자_아이디),
            저장된_져니_아이디);

        // then
        assertThat(deletedCount).isSameAs(2);
    }
}
