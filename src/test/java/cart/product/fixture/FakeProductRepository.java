package cart.product.fixture;

import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.test.util.ReflectionTestUtils;

public class FakeProductRepository implements ProductRepository {

    private final Map<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    @Override
    public Long save(Product product) {
        long id = idGen.incrementAndGet();
        ReflectionTestUtils.setField(product, "id", id);
        store.put(id, product);
        return id;
    }

    @Override
    public void update(Product product) {
        store.put(product.getId(), product);
    }

    @Override
    public void delete(Long productId) {
        store.remove(productId);
    }

    @Override
    public Product findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }
}
