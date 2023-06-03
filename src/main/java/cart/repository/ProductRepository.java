package cart.repository;

import cart.dao.OrdersCartItemDao;
import cart.dao.ProductDao;
import org.springframework.stereotype.Component;

@Component
public class ProductRepository {
    private final ProductDao productDao;
    private final OrdersCartItemDao ordersCartItemDao;

    public ProductRepository(ProductDao productDao, OrdersCartItemDao ordersCartItemDao) {
        this.productDao = productDao;
        this.ordersCartItemDao = ordersCartItemDao;
    }

    private int getPriceById(final long id) {
        return productDao.findPriceById(id);
    }

    public int findProductIdQuantityWithOrdersId(final long ordersId) {
        return ordersCartItemDao.findAllByOrdersId(ordersId).stream()
                .map(entry -> getPriceById(entry.getProductId()) * entry.getQuantity())
                .reduce(0, Integer::sum);
    }
}
