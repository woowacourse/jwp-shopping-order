package cart.application.service.cartitem;

import cart.application.repository.cartItem.CartItemRepository;
import cart.application.repository.member.MemberRepository;
import cart.application.repository.product.ProductRepository;
import cart.application.service.cartitem.dto.CartItemCreateDto;
import cart.application.service.cartitem.dto.CartItemUpdateDto;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemWriteService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public CartItemWriteService(final CartItemRepository cartItemRepository, final MemberRepository memberRepository, final ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Long createCartItem(Member memberAuth, CartItemCreateDto cartItemCreateDto) {
        final Member member = memberRepository.getById(memberAuth.getId());
        final Product product = productRepository.getById(cartItemCreateDto.getProductId());

        return cartItemRepository.createCartItem(new CartItem(product, member));
    }

    public void updateQuantity(Member memberAuth, Long cartItemId, CartItemUpdateDto cartItemUpdateDto) {
        final Member member = memberRepository.getById(memberAuth.getId());

        final CartItem cartItem = cartItemRepository.getById(cartItemId);

        cartItem.checkOwner(member);

        if (cartItemUpdateDto.getQuantity() == 0) {
            cartItemRepository.deleteById(cartItemId);
            return;
        }

        cartItem.changeQuantity(cartItemUpdateDto.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member memberAuth, Long cartItemId) {
        final Member member = memberRepository.getById(memberAuth.getId());
        final CartItem cartItem = cartItemRepository.getById(cartItemId);

        cartItem.checkOwner(member);

        cartItemRepository.deleteById(cartItemId);
    }

}
