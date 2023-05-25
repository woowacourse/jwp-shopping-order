package cart.service;

import static java.util.stream.Collectors.toList;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.dto.CartItemSaveRequest;
import cart.dto.CartItemSearchResponse;
import cart.dto.ProductDto;
import cart.exception.ProductNotFoundException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CartItemService {

    private final CartItemDao cartItemDao;

    public CartItemService(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public Long save(final Long memberId, final CartItemSaveRequest request) {
        final CartItem cartItem = new CartItem(memberId, request.getProductId());
        return cartItemDao.saveAndGetId(cartItem);
    }

    @Transactional(readOnly = true)
    public CartItemSearchResponse findAll(final Long memberId) {
        return cartItemDao.findAllProductByMemberId(memberId).stream()
                .map(ProductDto::from)
                .collect(Collectors.collectingAndThen(toList(), CartItemSearchResponse::new));
    }

    public void delete(final Long productId, final Long memberId) {
        final int affectedCount = cartItemDao.delete(productId, memberId);
        if (affectedCount == 0) {
            throw new ProductNotFoundException();
        }
    }
}
