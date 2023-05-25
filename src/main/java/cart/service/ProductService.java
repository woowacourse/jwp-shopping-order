package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.ProductDto;
import cart.dto.ProductSaveRequest;
import cart.dto.ProductUpdateRequest;
import cart.exception.ProductNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Long save(final ProductSaveRequest request) {
        final Product product = new Product(request.getName(), request.getImageUrl(), request.getPrice());
        return productDao.save(product).getId();
    }

    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productDao.findAll().stream()
                .map(ProductDto::from)
                .collect(toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public ProductDto findById(final Long id) {
        final Product product = productDao.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return ProductDto.from(product);
    }

    public void update(final Long id, final ProductUpdateRequest request) {
        final Product savedProduct = new Product(id, request.getName(), request.getImageUrl(), request.getPrice());
        final int affectedCount = productDao.update(savedProduct);
        if (affectedCount == 0) {
            throw new ProductNotFoundException();
        }
    }

    public void delete(final Long id) {
        final int affectedCount = productDao.delete(id);
        if (affectedCount == 0) {
            throw new ProductNotFoundException();
        }
    }
}
