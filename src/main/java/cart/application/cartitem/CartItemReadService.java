package cart.application.cartitem;

import cart.application.cartitem.dto.CartResultDto;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.repository.cartitem.CartItemRepository;
import cart.domain.repository.member.MemberRepository;
import cart.ui.MemberAuth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class CartItemReadService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public CartItemReadService(CartItemRepository cartItemRepository, MemberRepository memberRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    public CartResultDto findByMember(MemberAuth memberAuth) {
        Member member = memberRepository.findMemberById(memberAuth.getId())
                .orElseThrow(() -> new NoSuchElementException("일치하는 사용자가 없습니다."));

        CartItems cartItems = cartItemRepository.findAllCartItemsByMemberId(member.getId());
        int totalPrice = cartItems.calculateTotalPrice();

        return CartResultDto.of(cartItems.getCartItems(), totalPrice);
    }

}
