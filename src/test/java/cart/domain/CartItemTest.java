package cart.domain;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.domain.vo.Money;
import cart.domain.vo.Quantity;
import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static cart.fixture.domain.CartItemFixture.장바구니_상품;
import static cart.fixture.domain.MemberFixture.회원;
import static cart.fixture.domain.MoneyFixture.금액;
import static cart.fixture.domain.MoneyFixture.포인트;
import static cart.fixture.domain.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CartItemTest {

    private final Product 상품 = 상품("계란", "1000", "https://i.guim.co.uk/img/media/26392d05302e02f7bf4eb143bb84c8097d09144b/446_167_3683_2210/master/3683.jpg?width=1200&quality=85&auto=format&fit=max&s=a52bbe202f57ac0f5ff7f47166906403");
    private final Member 회원 = 회원("a@a.com", "1234a!", 금액("1000"), 포인트("1000"));

    @ParameterizedTest
    @ValueSource(ints = {-1, -10000})
    void ZERO_이하의_수를_입력하면_예외를_던진다(int 장바구니_상품_수량) {
        assertThatThrownBy(() -> new CartItem(상품, 회원, 장바구니_상품_수량))
                .isInstanceOf(CartItemException.InvalidQuantity.class)
                .hasMessageContaining("장바구니에 담긴 상품의 개수는 최소 0 이상이어야 합니다.");
    }

    @Test
    void 상품이_없으면_예외를_던진다() {
        assertThatThrownBy(() -> new CartItem(null, 회원, 10))
                .isInstanceOf(CartItemException.InvalidProduct.class)
                .hasMessageContaining("장바구니에 담으려는 상품이 존재하지 않습니다.");
    }

    @Test
    void 상품_총금액을_구한다() {
        // given
        CartItem 장바구니_상품 = 장바구니_상품(상품, 회원, 10);

        // when
        Money 총금액 = 장바구니_상품.totalPrice();

        // then
        assertThat(총금액).isEqualTo(Money.from(10000));
    }

    @Test
    void 같은_상품을_추가할_경우_장바구니_상품_수량을_합친다() {
        // given
        CartItem 첫_번째_장바구니_상품 = 장바구니_상품(상품, 회원, 10);
        CartItem 두_번째_장바구니_상품 = 장바구니_상품(상품, 회원, 10);

        // when
        CartItem 장바구니_상품 = 첫_번째_장바구니_상품.mergePlus(두_번째_장바구니_상품);

        // then
        assertThat(장바구니_상품).isEqualTo(new CartItem(상품, 회원, 20));
    }

    @Test
    void 같은_상품을_삭제할_경우_장바구니_상품_수량을_뺀다() {
        // given
        CartItem 첫_번째_장바구니_상품 = 장바구니_상품(상품, 회원, 10);
        CartItem 두_번째_장바구니_상품 = 장바구니_상품(상품, 회원, 10);

        // when
        CartItem 장바구니_상품 = 첫_번째_장바구니_상품.mergeMinus(두_번째_장바구니_상품);

        // then
        assertThat(장바구니_상품).isEqualTo(new CartItem(상품, 회원, 0));
    }

    @Test
    void 장바구니_상품_수량을_수정한다() {
        // given
        CartItem 장바구니_상품 = 장바구니_상품(상품, 회원, 10);

        // when
        CartItem 수정한_장바구니_상품 = 장바구니_상품.changeQuantity(Quantity.from(1));

        // then
        assertThat(수정한_장바구니_상품).isEqualTo(new CartItem(상품, 회원, 1));
    }

    @Test
    void 장바구니_상품의_주인인지_확인하고_아닐_경우_예외가_발생한다() {
        // given
        Member 첫_번째_회원 = 회원(1L, "a@a.com", "1234", 금액("1000"), 포인트("1000"));
        Member 두_번째_회원 = 회원(2L, "b@b.com", "4321", 금액("10"), 포인트("0"));

        CartItem 장바구니_상품 = 장바구니_상품(상품, 첫_번째_회원, 10);

        // expect
        assertThatThrownBy(() -> 장바구니_상품.checkOwner(두_번째_회원))
                .isInstanceOf(CartItemException.InvalidMember.class);
    }

    @Test
    void 장바구니_상품이_같은_상품인지_확인한다() {
        // given
        CartItem 첫_번째_장바구니 = 장바구니_상품(상품, 회원, 10);
        CartItem 두_번째_장바구니_상품 = 장바구니_상품(상품, 회원, 10000);

        // when
        boolean 같은_장바구니_상품인지 = 첫_번째_장바구니.isSameProduct(두_번째_장바구니_상품);

        // then
        assertThat(같은_장바구니_상품인지).isTrue();
    }
}
