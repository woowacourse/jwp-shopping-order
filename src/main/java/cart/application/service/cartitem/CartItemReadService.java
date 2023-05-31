package cart.application.service.cartitem;

import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.service.cartitem.dto.CartItemResultDto;
import cart.domain.Member;
import cart.domain.cartitem.CartItem;
import cart.ui.MemberAuth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CartItemReadService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public CartItemReadService(CartItemRepository cartItemRepository, final MemberRepository memberRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    public List<CartItemResultDto> findByMember(final MemberAuth memberAuth) {
        final Member member = memberRepository.findMemberById(memberAuth.getId())
                .orElseThrow(() -> new NoSuchElementException("일치하는 사용자가 없습니다."));

        List<CartItem> cartItems = cartItemRepository.findAllCartItemsByMemberId(member.getId());
        return cartItems.stream().map(CartItemResultDto::of).collect(Collectors.toList());
    }

}
