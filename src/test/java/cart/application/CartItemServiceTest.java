package cart.application;

import cart.dao.CartItemDao;
import cart.domain.cartitem.CartItem;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static cart.fixtures.CartItemFixtures.MemberA_CartItem1;
import static cart.fixtures.MemberFixtures.MemberA;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemServiceTest {

    @InjectMocks
    CartItemService cartItemService;

    @Mock
    CartItemDao cartItemDao;

    @Test
    void 멤버를_통해_장바구니_목록을_가져온다() {
        // given
        when(cartItemDao.findByMemberId(MemberA.ID))
                .thenReturn(List.of(MemberA_CartItem1.ENTITY));

        // when
        final List<CartItem> cartItems = cartItemService.findByMember(MemberA.ENTITY);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartItems).hasSize(1);
            softAssertions.assertThat(cartItems.get(0).getId()).isEqualTo(MemberA_CartItem1.ID);
            softAssertions.assertThat(cartItems.get(0).getQuantity()).isEqualTo(MemberA_CartItem1.QUANTITY);
            softAssertions.assertThat(cartItems.get(0).getProduct()).isEqualTo(MemberA_CartItem1.PRODUCT);
            softAssertions.assertThat(cartItems.get(0).getMember()).isEqualTo(MemberA_CartItem1.MEMBER);
        });
    }

    @Test
    void 멤버와_상품_id를_통해_장바구니를_가져온다() {
        // given
        when(cartItemDao.findByMemberIdAndProductId(MemberA.ID, MemberA_CartItem1.ID))
                .thenReturn(MemberA_CartItem1.ENTITY);

        // when
        final CartItem cartItem = cartItemService.findByMemberAndProduct(MemberA.ENTITY, CHICKEN.ENTITY);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartItem.getId()).isEqualTo(MemberA_CartItem1.ID);
            softAssertions.assertThat(cartItem.getQuantity()).isEqualTo(MemberA_CartItem1.QUANTITY);
            softAssertions.assertThat(cartItem.getProduct()).isEqualTo(CHICKEN.ENTITY);
            softAssertions.assertThat(cartItem.getMember()).isEqualTo(MemberA.ENTITY);
        });
    }

    @Test
    void 장바구니를_추가하다() {
        // given
        when(cartItemDao.save(MemberA_CartItem1.ENTITY))
                .thenReturn(3L);

        // when
        final Long cartItemId = cartItemService.add(MemberA_CartItem1.ENTITY);

        // then
        assertThat(cartItemId).isEqualTo(3L);
    }

    @Nested
    class updateQuantity_테스트 {

        @Test
        void 장바구니_상품의_개수가_0개이면_장바구니를_삭제하다() {
            // given
            when(cartItemDao.findById(MemberA_CartItem1.ID))
                    .thenReturn(MemberA_CartItem1.ENTITY);
            doNothing().when(cartItemDao).deleteById(MemberA_CartItem1.ID);

            // when, then
            assertDoesNotThrow(() -> cartItemService.updateQuantity(MemberA.ENTITY, 1L, 0));
        }

        @Test
        void 장바구니_상품의_개수가_0개가_아니면_장바구니를_업데이트하다() {
            // given
            when(cartItemDao.findById(MemberA_CartItem1.ID))
                    .thenReturn(MemberA_CartItem1.ENTITY);
            doNothing().when(cartItemDao).updateQuantity(MemberA_CartItem1.ENTITY);

            // when, then
            assertDoesNotThrow(() -> cartItemService.updateQuantity(MemberA.ENTITY, 1L, 10));
        }
    }

    @Test
    void 장바구니를_삭제하다() {
        // given
        when(cartItemDao.findById(MemberA_CartItem1.ID))
                .thenReturn(MemberA_CartItem1.ENTITY);
        doNothing().when(cartItemDao).deleteById(MemberA_CartItem1.ID);

        // when, then
        assertDoesNotThrow(() -> cartItemService.remove(MemberA.ENTITY, MemberA_CartItem1.ID));
    }
}
