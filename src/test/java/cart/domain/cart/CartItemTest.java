package cart.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class CartItemTest {

    @Test
    void 장바구니의_하나의_목록은_총_금액을_계산할_수_있다(){
        // given
        final Member member = new Member(1L, new Email("a@mail.com"), new Password("1234"));
        final Quantity quantity = new Quantity(5);
        final Product product = new Product(1L, new Name("상품"), new ImageUrl("example.com"), new Price(1000));
        final CartItem cartItem = new CartItem(1L, quantity, product, member);

        //when
        final Price totalPrice = cartItem.getTotalPrice();

        //then
        assertThat(totalPrice.price()).isEqualTo(5000);
    }
}
