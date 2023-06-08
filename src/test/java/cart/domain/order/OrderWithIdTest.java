package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cartitem.CartItem;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.product.Product;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderTest {

    @ParameterizedTest(name = "주문에 존재하는 사용자 이름과 요청받은 사용자의 이름이 다르면 false, 같으면 true를 반환한다.")
    @CsvSource(value = {"journey:false", "raon:true"}, delimiter = ':')
    void isNotOwner(final String name, final boolean expected) {
        // given
        final Member 져니 = Member.create(1L, "journey", EncryptedPassword.create("password"));
        final CartItem 치킨_장바구니_아이템 = new CartItem(1L, 10, new Product(1L,
            "치킨", 20000, "chicken_image_url", false));
        final CartItem 피자_장바구니_아이템 = new CartItem(2L, 5, new Product(2L,
            "피자", 30000, "pizza_image_url", false));
        final BasicOrder 주문 = new BasicOrder(1L, 져니, 3000, LocalDateTime.now(),
            List.of(치킨_장바구니_아이템, 피자_장바구니_아이템), true);

        // when
        final boolean result = 주문.isNotOwner(name);

        // then
        assertThat(result)
            .isSameAs(expected);
    }
}
