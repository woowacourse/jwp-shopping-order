package cart.product.application;

import cart.cartitem.dao.CartItemDao;
import cart.member.domain.Member;
import cart.product.application.dto.ProductCartItemDto;
import cart.product.dao.ProductDao;
import cart.product.domain.Product;
import cart.product.exception.NotFoundProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final int NOT_EXIST_PRODUCT = 0;

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public ProductService(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Transactional(readOnly = true)
    public Product getProductById(final Long productId) {
        validateExistProduct(productId);

        return productDao.getProductById(productId);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsInPaging(final Long lastIdInPrevPage, final int pageItemCount) {
        return getProductsByInterval(lastIdInPrevPage, pageItemCount);
    }

    private List<Product> getProductsByInterval(final Long lastIdInPrevPage, final int pageItemCount) {
        if (lastIdInPrevPage != 0) {
            return productDao.getProductByInterval(lastIdInPrevPage, pageItemCount);
        }

        final Long lastId = productDao.getLastProductId();
        return productDao.getProductByInterval(lastId + 1, pageItemCount);
    }

    @Transactional(readOnly = true)
    public boolean hasLastProduct(final Long lastIdInPrevPage, final int pageItemCount) {
        final List<Product> products = getProductsByInterval(lastIdInPrevPage, pageItemCount);
        return products.get(products.size() - 1).getId() == 1L;
    }

    @Transactional
    public Long createProduct(final Product product) {
        return productDao.createProduct(product);
    }

    @Transactional
    public void updateProduct(final Long productId, final Product product) {
        validateExistProduct(productId);

        productDao.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        validateExistProduct(productId);

        productDao.deleteProduct(productId);
    }

    @Transactional(readOnly = true)
    public List<ProductCartItemDto> getProductCartItemsByProduct(final Member member, final List<Product> products) {
        return products.stream()
                .map(product -> getProductCartItemByProduct(member, product))
                .collect(Collectors.toList());
    }

    private ProductCartItemDto getProductCartItemByProduct(final Member member, final Product product) {
        final Long memberId = member.getId();
        final Long productId = product.getId();

        return cartItemDao.findByMemberIdAndProductId(memberId, productId)
                .map(cartItem -> new ProductCartItemDto(product, cartItem))
                .orElse(new ProductCartItemDto(product, null));
    }

    private void validateExistProduct(final Long productId) {
        if (productDao.countById(productId) == NOT_EXIST_PRODUCT) {
            throw new NotFoundProductException();
        }
    }
}
