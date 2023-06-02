package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.dao.dto.CartItemDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CartItemDaoTest extends DaoTestHelper {

    @Autowired
    private CartItemDao cartItemDao;

    @Test
    @DisplayName("장바구니 정보를 저장한다.")
    void insert() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_장바구니_아이디 = 장바구니_치킨_저장(저장된_져니_아이디, 저장된_치킨_아이디);

        // then
        final CartItemDto cart = cartItemDao.findById(저장된_장바구니_아이디).get();
        assertThat(cart)
            .extracting(CartItemDto::getCartId, CartItemDto::getMemberId, CartItemDto::getMemberName,
                CartItemDto::getMemberPassword, CartItemDto::getProductId, CartItemDto::getProductName,
                CartItemDto::getProductPrice, CartItemDto::getProductImageUrl, CartItemDto::getProductQuantity)
            .containsExactly(저장된_장바구니_아이디, 저장된_져니_아이디, "journey", "password", 저장된_치킨_아이디,
                "치킨", 20000, "chicken_image_url", 1);
    }

    @Test
    @DisplayName("사용자의 장바구니 정보를 조회한다.")
    void findByMemberName() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_피자_아이디 = 피자_저장();
        final Long 저장된_장바구니_치킨_아이디 = 장바구니_치킨_저장(저장된_져니_아이디, 저장된_치킨_아이디);
        final Long 저장된_장바구니_피자_아이디 = 장바구니_피자_저장(저장된_져니_아이디, 저장된_피자_아이디);

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
    void findById_success() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_장바구니_아이디 = 장바구니_치킨_저장(저장된_져니_아이디, 저장된_치킨_아이디);

        // when
        final CartItemDto cart = cartItemDao.findById(저장된_장바구니_아이디).get();

        // then
        assertThat(cart)
            .extracting(CartItemDto::getCartId, CartItemDto::getMemberId, CartItemDto::getMemberName,
                CartItemDto::getProductId, CartItemDto::getProductName, CartItemDto::getProductPrice,
                CartItemDto::getProductImageUrl, CartItemDto::getProductQuantity)
            .containsExactly(저장된_장바구니_아이디, 저장된_져니_아이디, "journey", 저장된_치킨_아이디,
                "치킨", 20000, "chicken_image_url", 1);
    }

    @Test
    @DisplayName("유효한 장바구니 아이디가 없으면 빈 값을 반환한다.")
    void findById_empty() {
        // when
        final Optional<CartItemDto> cart = cartItemDao.findById(1L);

        // then
        assertThat(cart)
            .isEmpty();
    }

    @Test
    @DisplayName("장바구니의 수량 정보를 업데이트한다.")
    void updateQuantity() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_장바구니_아이디 = 장바구니_치킨_저장(저장된_져니_아이디, 저장된_치킨_아이디);

        // when
        cartItemDao.updateQuantity(저장된_장바구니_아이디, 10);

        // then
        final CartItemDto cart = cartItemDao.findById(저장된_장바구니_아이디).get();

        // then
        assertThat(cart)
            .extracting(CartItemDto::getCartId, CartItemDto::getMemberId, CartItemDto::getMemberName,
                CartItemDto::getProductId, CartItemDto::getProductName, CartItemDto::getProductPrice,
                CartItemDto::getProductImageUrl, CartItemDto::getProductQuantity)
            .containsExactly(저장된_장바구니_아이디, 저장된_져니_아이디, "journey", 저장된_치킨_아이디,
                "치킨", 20000, "chicken_image_url", 10);
    }

    @Test
    @DisplayName("장바구니의 정보를 삭제한다.")
    void deleteById() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_장바구니_아이디 = 장바구니_치킨_저장(저장된_져니_아이디, 저장된_치킨_아이디);

        // when
        final int deletedCount = cartItemDao.deleteById(저장된_장바구니_아이디);

        // then
        assertThat(deletedCount).isSameAs(1);
    }

    @Test
    @DisplayName("사용자의 아이디와 장바구니 상품 아이디로 장바구니 상품 개수를 조회한다.")
    void countByIdsAndMemberId() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_피자_아이디 = 피자_저장();
        final Long 저장된_장바구니_치킨_아이디 = 장바구니_치킨_저장(저장된_져니_아이디, 저장된_치킨_아이디);
        final Long 저장된_장바구니_피자_아이디 = 장바구니_피자_저장(저장된_져니_아이디, 저장된_피자_아이디);

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
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_피자_아이디 = 피자_저장();
        final Long 저장된_장바구니_치킨_아이디 = 장바구니_치킨_저장(저장된_져니_아이디, 저장된_치킨_아이디);
        final Long 저장된_장바구니_피자_아이디 = 장바구니_피자_저장(저장된_져니_아이디, 저장된_피자_아이디);

        // when
        final int deletedCount = cartItemDao.deleteByIdsAndMemberId(List.of(저장된_장바구니_치킨_아이디, 저장된_장바구니_피자_아이디),
            저장된_져니_아이디);

        // then
        assertThat(deletedCount)
            .isSameAs(2);
    }

    @Test
    @DisplayName("사용자 이름과 상품 아이디로 이미 등록이 되어 있으면 true를 반환한다.")
    void existByMemberNameAndProductId_true() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_치킨_아이디 = 치킨_저장();
        장바구니_치킨_저장(저장된_져니_아이디, 저장된_치킨_아이디);

        // when
        final boolean result = cartItemDao.existByMemberNameAndProductId("journey", 저장된_치킨_아이디);

        // then
        assertThat(result)
            .isTrue();
    }

    @Test
    @DisplayName("사용자 이름과 상품 아이디로 등록되어 있지 않으면 false를 반환한다.")
    void existByMemberNameAndProductId_false() {
        // when
        final boolean result = cartItemDao.existByMemberNameAndProductId("journey", 1L);

        // then
        assertThat(result)
            .isFalse();
    }

    @Test
    @DisplayName("사용자 이름과 상품 아이디로 장바구니를 제거한다.")
    void deleteByProductIdsAndMemberName() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_피자_아이디 = 피자_저장();
        장바구니_치킨_저장(저장된_져니_아이디, 저장된_치킨_아이디);
        장바구니_피자_저장(저장된_져니_아이디, 저장된_피자_아이디);

        // when
        final int deletedCount = cartItemDao.deleteByProductIdsAndMemberName(List.of(저장된_치킨_아이디, 저장된_피자_아이디),
            "journey");

        // then
        assertThat(deletedCount)
            .isSameAs(2);
    }
}
