package cart.application.cartitem;

import cart.application.cartitem.dto.CartItemCreateDto;
import cart.application.cartitem.dto.CartItemUpdateDto;
import cart.domain.Product;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.repository.cartitem.CartItemRepository;
import cart.domain.repository.member.MemberRepository;
import cart.domain.repository.product.ProductRepository;
import cart.ui.MemberAuth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class CartItemWriteService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public CartItemWriteService(CartItemRepository cartItemRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Long createCartItem(MemberAuth memberAuth, CartItemCreateDto cartItemCreateDto) {
        Member member = memberRepository.findMemberById(memberAuth.getId())
                .orElseThrow(() -> new NoSuchElementException("일치하는 사용자가 없습니다."));
        Product product = productRepository.findById(cartItemCreateDto.getProductId())
                .orElseThrow(() -> new NoSuchElementException("일치하는 상품이 없습니다."));
        return cartItemRepository.createCartItem(new CartItem(product, member));
    }

    public void updateQuantity(MemberAuth memberAuth, Long cartItemId, CartItemUpdateDto cartItemUpdateDto) {
        Member member = memberRepository.findMemberById(memberAuth.getId())
                .orElseThrow(() -> new NoSuchElementException("일치하는 사용자가 없습니다."));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 상품이 없습니다."));
        cartItem.checkOwner(member);

        if (cartItemUpdateDto.getQuantity() == 0) {
            cartItemRepository.deleteById(cartItemId);
            return;
        }

        cartItem.changeQuantity(cartItemUpdateDto.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(MemberAuth memberAuth, Long cartItemId) {
        Member member = memberRepository.findMemberById(memberAuth.getId())
                .orElseThrow(() -> new NoSuchElementException("일치하는 사용자가 없습니다."));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 상품이 없습니다."));
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(cartItemId);
    }

}
