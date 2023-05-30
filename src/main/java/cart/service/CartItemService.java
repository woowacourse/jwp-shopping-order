package cart.service;

import cart.domain.CartItem;
import cart.domain.member.Member;
import cart.domain.Product;
import cart.domain.member.MemberValidator;
import cart.dto.CartItemDto;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemSaveRequest;
import cart.exception.CartItemNotFoundException;
import cart.exception.MemberNotFoundException;
import cart.exception.ProductNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        final CartItem cartItem = new CartItem(memberId, product);
        return cartItemRepository.save(cartItem).getId();
    }

    @Transactional(readOnly = true)
    public List<CartItemDto> findAll(final Long memberId) {
        return cartItemRepository.findAllByMemberId(memberId).stream()
                .map(CartItemDto::from)
                .collect(toList());
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

    public void updateQuantity(
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
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
    }
}
