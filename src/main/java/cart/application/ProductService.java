package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.ProductCartItemDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public ProductService(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public Product getProductById(final Long productId) {
        return productDao.getProductById(productId);
    }

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

    public boolean hasLastProduct(final Long lastIdInPrevPage, final int pageItemCount) {
        final List<Product> products = getProductsByInterval(lastIdInPrevPage, pageItemCount);
        return products.get(products.size() - 1).getId() == 1L;
    }

    public Long createProduct(final Product product) {
        return productDao.createProduct(product);
    }

    public void updateProduct(final Long productId, final Product product) {
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }

    public List<ProductCartItemDto> getProductCartItemsByProduct(final Member member, final List<Product> products) {
        return products.stream()
                .map(product -> getProductCartItemByProduct(member, product))
                .collect(Collectors.toList());
    }

    private ProductCartItemDto getProductCartItemByProduct(final Member member, final Product product) {
        try {
            final CartItem cartItem = cartItemDao.findByMemberIdAndProductId(member.getId(), product.getId());
            return new ProductCartItemDto(product, cartItem);
        } catch (final NullPointerException e) {
            return new ProductCartItemDto(product, null);
        }
    }
}
