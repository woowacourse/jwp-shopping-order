package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.member.MemberWithId;
import cart.domain.product.Product;
import cart.domain.product.ProductWithId;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderWithIdTest {

    @ParameterizedTest(name = "주문에 존재하는 사용자 이름과 요청받은 사용자의 이름이 다르면 false, 같으면 true를 반환한다.")
    @CsvSource(value = {"journey:false", "raon:true"}, delimiter = ':')
    void isNotOwner(final String name, final boolean expected) {
        // given
        final MemberWithId 져니 = new MemberWithId(1L,
            Member.create("journey", EncryptedPassword.create("password")));
        final CartItemWithId 치킨_장바구니_아이템 = new CartItemWithId(1L, 10, new ProductWithId(1L,
            new Product("치킨", 20000, "chicken_image_url", false)));
        final CartItemWithId 피자_장바구니_아이템 = new CartItemWithId(2L, 5, new ProductWithId(2L,
            new Product("피자", 30000, "pizza_image_url", false)));
        final BasicOrder 주문 = new BasicOrder(져니, 3000, LocalDateTime.now(),
            List.of(치킨_장바구니_아이템, 피자_장바구니_아이템), true);
        final OrderWithId 아이디가_존재하는_주문 = new OrderWithId(1L, 주문);

        // when
        final boolean result = 아이디가_존재하는_주문.isNotOwner(name);

        // then
        assertThat(result)
            .isSameAs(expected);
    }
}
