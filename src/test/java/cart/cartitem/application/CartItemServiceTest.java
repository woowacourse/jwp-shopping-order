package cart.cartitem.application;

import cart.cartitem.dao.CartItemDao;
import cart.cartitem.domain.CartItem;
import cart.cartitem.exception.CartItemException;
import cart.cartitem.exception.NotFoundCartItemException;
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
import static cart.fixtures.MemberFixtures.Member_Dooly;
import static cart.fixtures.MemberFixtures.Member_Ber;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
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
    void 특정_유저의_장바구니_목록을_확인하다() {
        // given
        when(cartItemDao.findByMemberId(Member_Dooly.ID))
                .thenReturn(List.of(MemberA_CartItem1.ENTITY));

        // when
        final List<CartItem> cartItems = cartItemService.findByMember(Member_Dooly.ENTITY);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cartItems).hasSize(1);
            softAssertions.assertThat(cartItems.get(0)).isEqualTo(MemberA_CartItem1.ENTITY);
        });
    }

    @Nested
    class findByMemberAndProduct_테스트 {

        @Test
        void 특정_유저가_가진_장바구니를_한_개_검색한다() {
            // given
            final Optional<CartItem> cartItem = Optional.of(MemberA_CartItem1.ENTITY);
            when(cartItemDao.findByMemberIdAndProductId(Member_Dooly.ID, CHICKEN.ID))
                    .thenReturn(cartItem);

            // when
            final CartItem actual = cartItemService.findByMemberAndProduct(Member_Dooly.ENTITY, CHICKEN.ENTITY);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).isEqualTo(MemberA_CartItem1.ENTITY);
            });
        }

        @Test
        void 특정_유저가_장바구니에_없는_상품을_가져오려고_하면_null을_반환하다() {
            // given
            final Optional<CartItem> nullCartItem = Optional.empty();
            when(cartItemDao.findByMemberIdAndProductId(Member_Dooly.ID, CHICKEN.ID))
                    .thenReturn(nullCartItem);

            // when
            final CartItem cartItem = cartItemService.findByMemberAndProduct(Member_Dooly.ENTITY, CHICKEN.ENTITY);

            // then
            assertThat(cartItem).isNull();
        }
    }

    @Nested
    class add_테스트 {

        @Test
        void 장바구니에_이미_존재하는_상품을_추가하면_수량을_늘린다() {
            // given
            final Optional<CartItem> cartItem = Optional.of(MemberA_CartItem1.ENTITY);
            when(cartItemDao.findByMemberIdAndProductId(Member_Dooly.ID, CHICKEN.ID))
                    .thenReturn(cartItem);
            doNothing().when(cartItemDao).updateQuantity(MemberA_CartItem1.ENTITY);

            // when
            final Long cartItemId = cartItemService.add(MemberA_CartItem1.ENTITY);

            // then
            assertThat(cartItemId).isEqualTo(MemberA_CartItem1.ID);

        }

        @Test
        void 장바구니에_존재하지_않는_상품을_추가하면_개수를_1개로_설정한다() {
            // given
            final Optional<CartItem> cartItem = Optional.empty();
            when(cartItemDao.findByMemberIdAndProductId(Member_Dooly.ID, CHICKEN.ID))
                    .thenReturn(cartItem);
            when(cartItemDao.save(MemberA_CartItem1.ENTITY))
                    .thenReturn(MemberA_CartItem1.ID);

            // when
            final Long cartItemId = cartItemService.add(MemberA_CartItem1.ENTITY);

            // then
            assertThat(cartItemId).isEqualTo(MemberA_CartItem1.ID);
        }
    }

    @Nested
    class updateQuantity_테스트 {

        @Test
        void 멤버의_장바구니_상품의_수량을_변경할_수_있다() {
            // given
            when(cartItemDao.countById(MemberA_CartItem1.ID)).thenReturn(1L);
            when(cartItemDao.findById(MemberA_CartItem1.ID))
                    .thenReturn(MemberA_CartItem1.ENTITY);
            doNothing().when(cartItemDao).updateQuantity(MemberA_CartItem1.ENTITY);

            // when, then
            assertDoesNotThrow(() -> cartItemService.updateQuantity(Member_Dooly.ENTITY, MemberA_CartItem1.ID, 5));
        }

        @Test
        void 멤버의_장바구니_상품의_개수가_0개이면_상품을_삭제하다() {
            // given
            final CartItem cartItem = CartItem.of(1L, 2, CHICKEN.ENTITY, Member_Dooly.ENTITY);
            when(cartItemDao.countById(cartItem.getId())).thenReturn(1L);
            when(cartItemDao.findById(1L)).thenReturn(cartItem);
            doNothing().when(cartItemDao).deleteById(1L);

            // when, then
            assertDoesNotThrow(() -> cartItemService.updateQuantity(Member_Dooly.ENTITY, 1L, 0));
        }

        @Test
        void 존재하지_않는_장바구니_상품에_접근하면_예외를_반환하다() {
            // given
            when(cartItemDao.countById(any())).thenReturn(0L);

            // when, then
            assertThatThrownBy(() -> cartItemService.updateQuantity(Member_Dooly.ENTITY, 1L, 1))
                    .isInstanceOf(NotFoundCartItemException.class)
                    .hasMessage("존재하지 않는 장바구니 상품입니다");
        }

        @Test
        void 멤버가_다른_멤버의_장바구니에_접근하면_예외를_반환하다() {
            // given
            final CartItem cartItem = CartItem.of(1L, 0, CHICKEN.ENTITY, Member_Dooly.ENTITY);
            when(cartItemDao.countById(1L)).thenReturn(1L);
            when(cartItemDao.findById(cartItem.getId())).thenReturn(cartItem);

            // when, then
            assertThatThrownBy(() -> cartItemService.updateQuantity(Member_Ber.ENTITY, 1L, 5))
                    .isInstanceOf(CartItemException.IllegalMember.class)
                    .hasMessage("Illegal member attempts to cart; cartItemId=1, memberId=2");
        }
    }

    @Nested
    class remove_테스트 {

        @Test
        void 상품을_제거하다() {
            // given
            when(cartItemDao.countById(MemberA_CartItem1.ID)).thenReturn(1L);
            when(cartItemDao.findById(MemberA_CartItem1.ID)).thenReturn(MemberA_CartItem1.ENTITY);
            doNothing().when(cartItemDao).deleteById(MemberA_CartItem1.ID);

            // when, then
            assertDoesNotThrow(() -> cartItemService.remove(Member_Dooly.ENTITY, MemberA_CartItem1.ID));
        }

        @Test
        void 존재하지_않는_장바구니_상품에_접근하면_예외를_반환하다() {
            // given
            when(cartItemDao.countById(any())).thenReturn(0L);

            // when, then
            assertThatThrownBy(() -> cartItemService.remove(Member_Dooly.ENTITY, 1L))
                    .isInstanceOf(NotFoundCartItemException.class)
                    .hasMessage("존재하지 않는 장바구니 상품입니다");
        }

        @Test
        void 멤버가_다른_멤버의_장바구니에_접근하면_예외를_반환하다() {
            // given
            when(cartItemDao.countById(MemberA_CartItem1.ID)).thenReturn(1L);
            when(cartItemDao.findById(MemberA_CartItem1.ID)).thenReturn(MemberA_CartItem1.ENTITY);

            // when, then
            assertThatThrownBy(() -> cartItemService.remove(Member_Ber.ENTITY, MemberA_CartItem1.ID))
                    .isInstanceOf(CartItemException.IllegalMember.class)
                    .hasMessage("Illegal member attempts to cart; cartItemId=1, memberId=2");
        }
    }
}
