package cart.application;

import cart.application.request.CreateCartItemRequest;
import cart.application.request.UpdateCartItemQuantityRequest;
import cart.application.response.CartItemResponse;
import cart.config.ServiceTestConfig;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemServiceTest extends ServiceTestConfig {

    CartItemService cartItemService;

    @BeforeEach
    void setUp() {
        cartItemService = new CartItemService(productRepository, cartItemRepository);
    }

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "100000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);

        CreateCartItemRequest 장바구니_계란_추가_요청 = new CreateCartItemRequest(계란.getId());

        // when
        Long 장바구니_계란_식별자값 = cartItemService.addCartItem(회원, 장바구니_계란_추가_요청);

        List<CartItemResponse> 회원_장바구니_상품_목록 = cartItemService.findByMember(회원);

        // then
        assertThat(회원_장바구니_상품_목록)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        CartItemResponse.of(new CartItem(계란, 회원, 1))
                ));
    }

    @Test
    void 장바구니_상품_수량을_수정할_때_해당_수량이_0일_경우_삭제된다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "100000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        CartItem 장바구니_계란 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);

        // when
        UpdateCartItemQuantityRequest 장바구니_상품_수량_수정_요청 = new UpdateCartItemQuantityRequest(0);
        cartItemService.updateQuantity(회원, 장바구니_계란.getId(), 장바구니_상품_수량_수정_요청);

        Optional<CartItem> 아마도_장바구니_계란 = cartItemDaoFixture.장바구니_상품을_조회한다(장바구니_계란.getId());

        // then
        assertThat(아마도_장바구니_계란).isEmpty();
    }

    @Test
    void 장바구니_상품_수량을_수정할_때_해당_수량이_1이상일_경우_수정된다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "100000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        CartItem 장바구니_계란 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);

        // when
        UpdateCartItemQuantityRequest 장바구니_상품_수량_수정_요청 = new UpdateCartItemQuantityRequest(1);
        cartItemService.updateQuantity(회원, 장바구니_계란.getId(), 장바구니_상품_수량_수정_요청);

        Optional<CartItem> 아마도_장바구니_계란 = cartItemDaoFixture.장바구니_상품을_조회한다(장바구니_계란.getId());

        // then
        assertThat(아마도_장바구니_계란).contains(new CartItem(계란, 회원, 1));
    }
}
