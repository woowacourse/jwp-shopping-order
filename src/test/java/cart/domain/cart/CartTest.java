package cart.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class CartTest {

    @Test
    void 장바구니에_담긴_모든_목록의_가격을_계산한다() {
        // given
        final Member member = new Member(1L, new Email("a@mail.com"), new Password("1234"));
        
        final Quantity quantity1 = new Quantity(5);
        final Quantity quantity2 = new Quantity(10);
        
        final Product product1 = new Product(1L, new Name("상품1"), new ImageUrl("example.com"), new Price(1000));
        final Product product2 = new Product(2L, new Name("상품2"), new ImageUrl("example.com"), new Price(100));
        
        final CartItem cartItem1 = new CartItem(1L, quantity1, product1, member);
        final CartItem cartItem2 = new CartItem(2L, quantity2, product2, member);
        
        final Cart cart = new Cart(List.of(cartItem1, cartItem2));
        
        // when
        final Price totalPrice = cart.getTotalPrice();

        // then
        assertThat(totalPrice.price()).isEqualTo(6000);
    }
}
