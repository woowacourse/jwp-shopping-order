package cart.service;

import cart.domain.CartItem;
import cart.domain.Product;
import cart.domain.member.Member;
import cart.domain.member.MemberValidator;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemSaveRequest;
import cart.exception.cart.CartItemNotFoundException;
import cart.exception.cart.ProductNotFoundException;
import cart.exception.member.MemberNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public CartItemService(
            final CartItemRepository cartItemDao,
            final MemberRepository memberRepository,
            final ProductRepository productDao
    ) {
        this.cartItemRepository = cartItemDao;
        this.memberRepository = memberRepository;
        this.productRepository = productDao;
    }

    public CartItem save(final Long memberId, final CartItemSaveRequest request) {
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        final CartItem cartItem = new CartItem(memberId, product);
        return cartItemRepository.save(cartItem);
    }

    @Transactional(readOnly = true)
    public List<CartItem> findAll(final Long memberId) {
        return cartItemRepository.findAllByMemberId(memberId);
    }

    public void delete(final Long cartItemId, final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);

        final MemberValidator memberValidator = new MemberValidator(member);
        cartItem.validateOwner(memberValidator);
        cartItemRepository.delete(cartItem);
    }

    public CartItem updateQuantity(
            final Long memberId,
            final Long cartItemId,
            final CartItemQuantityUpdateRequest request
    ) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        final MemberValidator memberValidator = new MemberValidator(member);
        cartItem.validateOwner(memberValidator);
        if (request.getQuantity() == 0) {
            cartItemRepository.delete(cartItem);
            return cartItem;
        }

        cartItem.changeQuantity(request.getQuantity());
        return cartItemRepository.save(cartItem);
    }
}
