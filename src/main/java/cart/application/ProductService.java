package cart.application;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.ProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public Product getProductById(Long productId) {
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

    public void updateProduct(Long productId, Product product) {
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
