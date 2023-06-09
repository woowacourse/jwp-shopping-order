package cart.domain.cartitem;

import cart.fixture.MemberFixture;
import cart.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class CartItemsTest {

    @DisplayName("장바구니에 담긴 물건들의 총 가격을 올바르게 반환한다.")
    @ParameterizedTest
    @CsvSource({"3, 4, 1, 23000", "1, 1, 10, 104000", "5, 5, 5, 70000"})
    void calculateTotalPriceTest(int count1, int count2, int count3, int totalPrice) {
        CartItems cartItems = new CartItems(List.of(
                new CartItem(count1, ProductFixture.꼬리요리, MemberFixture.비버),
                new CartItem(count2, ProductFixture.통구이, MemberFixture.비버),
                new CartItem(count3, ProductFixture.배변패드, MemberFixture.비버)
        ));
        assertThat(cartItems.calculateTotalPrice()).isEqualTo(totalPrice);
    }

    @DisplayName("전체 장바구니 중 하나라도 소유자가 다르면 true")
    @Test
    void isNotOwnedMemberTrueTest() {
        CartItems cartItems = new CartItems(List.of(
                new CartItem(2, ProductFixture.꼬리요리_ID포함, MemberFixture.디노_ID포함),
                new CartItem(2, ProductFixture.통구이_ID포함, MemberFixture.디노_ID포함),
                new CartItem(2, ProductFixture.배변패드_ID포함, MemberFixture.비버_ID포함)
        )
        );
        assertThat(cartItems.isNotOwnedMember(MemberFixture.디노_ID포함)).isTrue();
    }

    @DisplayName("전체 장바구니 모두 소유자가 같으면 false")
    @Test
    void isNotOwnedMemberFalseTest() {
        CartItems cartItems = new CartItems(List.of(
                new CartItem(2, ProductFixture.꼬리요리_ID포함, MemberFixture.디노_ID포함),
                new CartItem(2, ProductFixture.통구이_ID포함, MemberFixture.디노_ID포함),
                new CartItem(2, ProductFixture.배변패드_ID포함, MemberFixture.디노_ID포함)
        )
        );
        assertThat(cartItems.isNotOwnedMember(MemberFixture.디노_ID포함)).isFalse();
    }
}
