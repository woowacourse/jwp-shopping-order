package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductRepository {
    private final ProductDao productDao;


    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findByIds(final List<Long> productsIds){
        List<Product> products = new ArrayList<>();
        for(Long id: productsIds){
            products.add(productDao.findById(id));
        }
        return products;
    }
    public int getPriceById(final long id){
        return productDao.findPriceById(id);
    }
}
