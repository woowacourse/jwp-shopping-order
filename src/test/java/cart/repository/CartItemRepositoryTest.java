package cart.repository;

import cart.config.RepositoryTestConfig;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.entity.CartItemEntityFixture.장바구니_상품_엔티티;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemRepositoryTest extends RepositoryTestConfig {

    CartItemRepository cartItemRepository;

    Member 회원;
    Product 계란;

    @BeforeEach
    void setUp() {
        cartItemRepository = new CartItemRepository(cartItemDao);
        회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "10000", "1000");
        계란 = productDaoFixture.상품을_등록한다("계란", 1000);
    }

    @Test
    void 장바구니_상품을_저장한다() {
        // when
        Long 장바구니_상품_식별자값 = cartItemRepository.saveCartItem(장바구니_상품_엔티티(계란.getId(), 회원.getId(), 10));

        // then
        assertThat(장바구니_상품_식별자값)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 장바구니_상품_식별자값을_통해_장바구니_상품을_단건_조회한다() {
        // given
        Long 장바구니_상품_식별자값 = cartItemRepository.saveCartItem(장바구니_상품_엔티티(계란.getId(), 회원.getId(), 10));

        // when
        CartItem 장바구니_계란 = cartItemRepository.findByCartItemId(장바구니_상품_식별자값);

        // then
        assertThat(장바구니_계란)
                .isEqualTo(new CartItem(장바구니_상품_식별자값, 계란, 회원, 10));
    }

    @Test
    void 장바구니_상품_식별자값_목록을_통해_장바구니_상품_목록을_조회한다() {
        // given
        Long 장바구니_상품_식별자값 = cartItemRepository.saveCartItem(장바구니_상품_엔티티(계란.getId(), 회원.getId(), 10));

        // when
        CartItems 장바구니_상품_목록 = cartItemRepository.findByCartItemIds(List.of(장바구니_상품_식별자값));

        // then
        assertThat(장바구니_상품_목록)
                .isEqualTo(CartItems.from(List.of(
                        new CartItem(장바구니_상품_식별자값, 계란, 회원, 10))
                ));
    }

    @Test
    void 회원_식별자값을_통해_장바구니_상품_목록을_조회한다() {
        // given
        Long 장바구니_상품_식별자값 = cartItemRepository.saveCartItem(장바구니_상품_엔티티(계란.getId(), 회원.getId(), 10));

        // when
        CartItems 장바구니_상품_목록 = cartItemRepository.findByMemberId(회원.getId());

        // then
        assertThat(장바구니_상품_목록)
                .isEqualTo(CartItems.from(List.of(
                        new CartItem(장바구니_상품_식별자값, 계란, 회원, 10))
                ));
    }

    @Test
    void 장바구니_수량을_수정할_때_장바구니_수량이_0일_경우_삭제한다() {
        // given
        Long 장바구니_상품_식별자값 = cartItemRepository.saveCartItem(장바구니_상품_엔티티(계란.getId(), 회원.getId(), 10));

        // when
        cartItemRepository.updateQuantityOrDelete(장바구니_상품_엔티티(장바구니_상품_식별자값, 계란.getId(), 회원.getId(), 0));

        // then
        assertThatThrownBy(() -> cartItemRepository.findByCartItemId(장바구니_상품_식별자값))
                .isInstanceOf(CartItemException.NotFound.class);
    }

    @Test
    void 장바구니_수량을_수정할_때_장바구니_수량이_1이상일_경우_수정한다() {
        // given
        Long 장바구니_상품_식별자값 = cartItemRepository.saveCartItem(장바구니_상품_엔티티(계란.getId(), 회원.getId(), 10));

        // when
        cartItemRepository.updateQuantityOrDelete(장바구니_상품_엔티티(계란.getId(), 회원.getId(), 1));

        CartItem 장바구니_계란 = cartItemRepository.findByCartItemId(장바구니_상품_식별자값);

        // then
        assertThat(장바구니_계란)
                .isEqualTo(new CartItem(장바구니_상품_식별자값, 계란, 회원, 10));
    }
}
