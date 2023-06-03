package cart.service;

import static java.util.stream.Collectors.toList;

import cart.domain.cart.CartItem;
import cart.domain.cart.Product;
import cart.domain.member.Member;
import cart.dto.cart.CartItemDto;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemSaveRequest;
import cart.exception.cart.CartItemNotFoundException;
import cart.exception.cart.ProductNotFoundException;
import cart.exception.member.MemberNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Long save(final Long memberId, final CartItemSaveRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        final CartItem cartItem = new CartItem(member, product);
        return cartItemRepository.save(cartItem).getId();
    }

    @Transactional(readOnly = true)
    public List<CartItemDto> findAll(final Long memberId) {
        return cartItemRepository.findAllByMemberId(memberId).stream()
                .map(CartItemDto::from)
                .collect(toList());
    }

    public void delete(final Long cartItemId, final Long memberId) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        cartItem.checkOwner(member);
        cartItemRepository.deleteById(cartItemId);
    }

    public void updateQuantity(
            final Long memberId,
            final Long cartItemId,
            final CartItemQuantityUpdateRequest request
    ) {
        final CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        item.checkOwner(member);
        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(cartItemId);
            return;
        }

        item.changeQuantity(request.getQuantity());
        cartItemRepository.save(item);
    }
}
