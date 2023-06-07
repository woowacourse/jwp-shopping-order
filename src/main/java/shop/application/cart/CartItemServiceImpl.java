package shop.application.cart;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.application.cart.dto.CartDto;
import shop.domain.cart.CartItem;
import shop.domain.member.Member;
import shop.domain.product.Product;
import shop.domain.repository.CartRepository;
import shop.domain.repository.ProductRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class CartItemServiceImpl implements CartItemService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartItemServiceImpl(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    @Override
    public Long add(Member member, Long productId) {
        Product product = productRepository.findById(productId);

        return cartRepository.save(new CartItem(member, product));
    }

    @Override
    public List<CartDto> findByMember(Member member) {
        List<CartItem> cartItems = cartRepository.findAllByMemberId(member.getId());

        return CartDto.of(cartItems);
    }

    @Transactional
    @Override
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
    @Override
    public void remove(Member member, Long id) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        cartRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void removeItems(Member member, List<Long> ids) {
        List<CartItem> cartItems = cartRepository.findAllByMemberId(member.getId());
        cartItems.forEach(item -> item.checkOwner(member));

        cartRepository.deleteByIds(ids);
    }
}
