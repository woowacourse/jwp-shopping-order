package cart.application;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.domain.repository.CartRepository;
import cart.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartItemService(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Long add(Member member, Long productId) {
        Product product = productRepository.findById(productId);

        return cartRepository.save(new CartItem(member, product));
    }

    public List<CartItem> findByMember(Member member) {
        return cartRepository.findAllByMemberId(member.getId());
    }

    @Transactional
    public void updateQuantity(Member member, Long id, Integer quantity) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        if (quantity == 0) {
            cartRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(quantity);
        cartRepository.update(cartItem);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        cartRepository.deleteById(id);
    }

    @Transactional
    public void removeItems(final Member member, final List<Long> ids) {
        cartRepository.deleteByMemberIdAndCartItemIds(member.getId(), ids);
    }
}
