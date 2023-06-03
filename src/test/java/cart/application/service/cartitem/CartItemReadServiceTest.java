package cart.application.service.cartitem;

import cart.application.repository.cartItem.CartItemRepository;
import cart.application.repository.member.MemberRepository;
import cart.application.service.cartitem.dto.CartResultDto;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static cart.fixture.MemberFixture.레오_ID포함;
import static cart.fixture.ProductFixture.꼬리요리;
import static cart.fixture.ProductFixture.통구이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemReadServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CartItemReadService cartItemReadService;

    @Test
    @DisplayName("사용자의 해당하는 장바구니의 정보를 조회한다.")
    void findByMemberTest() {
        // given
        final Member memberAuth = new Member(레오_ID포함.getId(), "레오", "leo@gmail.com", "leo123");
        given(memberRepository.findMemberById(any()))
                .willReturn(Optional.of(레오_ID포함));
        given(cartItemRepository.findAllCartItemsByMemberId(any()))
                .willReturn(new CartItems(List.of(
                        new CartItem(3, 통구이, 레오_ID포함),
                        new CartItem(1, 꼬리요리, 레오_ID포함)
                )));
        // when
        final CartResultDto resultDto = cartItemReadService.findByMember(memberAuth);

        // then
        assertThat(resultDto.getTotalPrice()).isEqualTo(6000);
    }

}
