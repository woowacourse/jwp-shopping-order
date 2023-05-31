package cart.domain.order;

import cart.domain.Money;
import cart.domain.fixture.Fixture;
import cart.domain.member.MemberValidator;
import cart.exception.InvalidOrderOwnerException;
import cart.exception.InvalidOrderSizeException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문_상품이_1개_이상이어야_한다() {
        assertAll(
                () -> assertThatThrownBy(() -> Order.createFromCartItems(Collections.emptyList(), new Money(1000L), Fixture.memberCoupon1, 1L))
                        .isInstanceOf(InvalidOrderSizeException.class),
                () -> assertThatCode(() -> Order.createFromCartItems(List.of(Fixture.cartItem1), new Money(1000L), Fixture.memberCoupon1, 1L))
                        .doesNotThrowAnyException()
        );
    }

    @Test
    void 주문한_사용자인지_검증한다() {
        // given
        final Order orderFromMember1 = Order.createFromCartItems(List.of(Fixture.cartItem1), new Money(1000L), Fixture.memberCoupon1, Fixture.member1.getId());
        final Order orderFromMember2 = Order.createFromCartItems(List.of(Fixture.cartItem2), new Money(1000L), Fixture.memberCoupon2, Fixture.member2.getId());
        final MemberValidator memberValidator = new MemberValidator(Fixture.member1);

        // when & then
        assertAll(
                () -> assertThatCode(() -> orderFromMember1.validateMember(memberValidator))
                        .doesNotThrowAnyException(),
                () -> assertThatThrownBy(() -> orderFromMember2.validateMember(memberValidator))
                        .isInstanceOf(InvalidOrderOwnerException.class)
        );
    }
}