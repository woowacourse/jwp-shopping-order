package cart.application.service.cartitem;

import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.service.cartitem.dto.CartResultDto;
import cart.domain.Member;
import cart.domain.cartitem.CartItems;
import cart.ui.MemberAuth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class CartItemReadService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public CartItemReadService(CartItemRepository cartItemRepository, final MemberRepository memberRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    public CartResultDto findByMember(final MemberAuth memberAuth) {
        final Member member = memberRepository.findMemberById(memberAuth.getId())
                .orElseThrow(() -> new NoSuchElementException("일치하는 사용자가 없습니다."));

        final CartItems cartItems = cartItemRepository.findAllCartItemsByMemberId(member.getId());
        final int totalPrice = cartItems.calculateTotalPrice();

        return CartResultDto.of(cartItems.getCartItems(), totalPrice);
    }

}
