package cart.repository;

import cart.dao.OrdersCartItemDao;
import cart.dao.ProductDao;
import cart.dao.entity.OrdersCartItemEntity;
import cart.domain.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductRepository {
    private final ProductDao productDao;
    private final OrdersCartItemDao ordersCartItemDao;

    public ProductRepository(ProductDao productDao, OrdersCartItemDao ordersCartItemDao) {
        this.productDao = productDao;
        this.ordersCartItemDao = ordersCartItemDao;
    }
    public int getPriceById(final long id){
        return productDao.findPriceById(id);
    }
    public int findProductIdQuantityWithOrdersId(final long ordersId){
        return ordersCartItemDao.findAllByOrdersId(ordersId).stream()
                .map(entry -> getPriceById(entry.getProductId())*entry.getQuantity())
                .reduce(0,Integer::sum);
    }
}
