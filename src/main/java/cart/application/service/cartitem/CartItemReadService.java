package cart.application.service.cartitem;

import cart.application.repository.cartItem.CartItemRepository;
import cart.application.repository.member.MemberRepository;
import cart.application.service.cartitem.dto.CartResultDto;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartItemReadService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public CartItemReadService(CartItemRepository cartItemRepository, final MemberRepository memberRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    public CartResultDto findByMember(final Member memberAuth) {
        final Member member = memberRepository.getById(memberAuth.getId());
        final CartItems cartItems = cartItemRepository.findAllCartItemsByMemberId(member.getId());

        final int totalPrice = cartItems.calculateTotalPrice();

        return CartResultDto.of(cartItems.getCartItems(), totalPrice);
    }

}
