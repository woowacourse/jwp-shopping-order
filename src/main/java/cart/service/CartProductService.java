package cart.service;

import static java.util.stream.Collectors.toList;

import cart.dao.CartProductDao;
import cart.domain.CartProduct;
import cart.dto.CartProductSaveRequest;
import cart.dto.CartProductSearchResponse;
import cart.dto.ProductDto;
import cart.exception.ProductNotFoundException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CartProductService {

    private final CartProductDao cartProductDao;

    public CartProductService(final CartProductDao cartProductDao) {
        this.cartProductDao = cartProductDao;
    }

    public Long save(final Long memberId, final CartProductSaveRequest request) {
        final CartProduct cartProduct = new CartProduct(memberId, request.getProductId());
        return cartProductDao.saveAndGetId(cartProduct);
    }

    @Transactional(readOnly = true)
    public CartProductSearchResponse findAll(final Long memberId) {
        return cartProductDao.findAllProductByMemberId(memberId).stream()
                .map(ProductDto::from)
                .collect(Collectors.collectingAndThen(toList(), CartProductSearchResponse::new));
    }

    public void delete(final Long productId, final Long memberId) {
        final int affectedCount = cartProductDao.delete(productId, memberId);
        if (affectedCount == 0) {
            throw new ProductNotFoundException();
        }
    }
}
