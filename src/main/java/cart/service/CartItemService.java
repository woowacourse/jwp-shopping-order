package cart.service;

import static java.util.stream.Collectors.toList;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemDto;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemSaveRequest;
import cart.exception.CartItemNotFoundException;
import cart.exception.MemberNotFoundException;
import cart.exception.ProductNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CartItemService {

    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;
    private final ProductDao productDao;

    public CartItemService(final CartItemDao cartItemDao, final MemberDao memberDao, final ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
        this.productDao = productDao;
    }

    public Long save(final Long memberId, final CartItemSaveRequest request) {
        final Member member = memberDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        final Product product = productDao.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        final CartItem cartItem = new CartItem(member, product);
        return cartItemDao.save(cartItem).getId();
    }

    @Transactional(readOnly = true)
    public List<CartItemDto> findAll(final Long memberId) {
        return cartItemDao.findAllByMemberId(memberId).stream()
                .map(CartItemDto::from)
                .collect(toList());
    }

    public void delete(final Long productId, final Long memberId) {
        final int affectedCount = cartItemDao.delete(productId, memberId);
        if (affectedCount == 0) {
            throw new ProductNotFoundException();
        }
    }

    public void updateQuantity(
            final Long memberId,
            final Long cartItemId,
            final CartItemQuantityUpdateRequest request
    ) {
        final CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);

        final Member member = memberDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.delete(cartItemId, memberId);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }
}
