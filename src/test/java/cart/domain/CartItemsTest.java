package cart.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static cart.fixture.CartItemFixture.장바구니_상품;
import static cart.fixture.CartItemsFixture.장바구니_상품_목록;
import static cart.fixture.MemberFixture.회원;
import static cart.fixture.MoneyFixture.금액;
import static cart.fixture.MoneyFixture.포인트;
import static cart.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CartItemsTest {

    Product 계란;
    Member 회원;

    @BeforeEach
    void setUp() {
        계란 = 상품("계란", "1000", "https://계란_이미지_주소");
        회원 = 회원("a@a.com", "1234", 금액("1000"), 포인트("1000"));
    }

    @Test
    void 장바구니에_존재하지_않는_상품을_추가한다() {
        // given
        CartItems 장바구니_상품_목록 = CartItems.empty();

        // when
        CartItem 장바구니_상품 = 장바구니_상품(계란, 회원, 10);
        장바구니_상품_목록.add(장바구니_상품);

        List<CartItem> 모든_장바구니_상품_목록 = 장바구니_상품_목록.getCartItems();

        // then
        assertThat(모든_장바구니_상품_목록)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(
                        장바구니_상품(계란, 회원, 10)
                );
    }

    @Test
    void 장바구니에_존재하는_상품을_추가하는_경우_수량이_증가한다() {
        // when
        CartItems 장바구니_상품_목록 = 장바구니_상품_목록(new ArrayList<>(List.of(
                장바구니_상품(계란, 회원, 10)
        )));

        CartItem 장바구니_계란 = 장바구니_상품(계란, 회원, 10);
        장바구니_상품_목록.add(장바구니_계란);

        List<CartItem> 모든_장바구니_상품_목록 = 장바구니_상품_목록.getCartItems();

        // then
        assertThat(모든_장바구니_상품_목록)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(
                        장바구니_상품(계란, 회원, 20)
                );
    }

    @Test
    void 장바구니에_특정_상품을_제거한다() {
        // given
        CartItems 장바구니_상품_목록 = 장바구니_상품_목록(new ArrayList<>(List.of(
                장바구니_상품(계란, 회원, 10)
        )));
        // when
        장바구니_상품_목록.remove(장바구니_상품(계란, 회원, 10));

        List<CartItem> 모든_장바구니_상품_목록 = 장바구니_상품_목록.getCartItems();

        // then
        assertThat(모든_장바구니_상품_목록).isEmpty();
    }
}
