package cart.sale;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class SaleRepository {
    private final Map<Long, Sale> couponMap = new HashMap<>();
    private long id = 0L;

    public Long save(String name, Long discountPolicyId) {
        final var id = this.id++;
        this.couponMap.put(id, new Sale(name, discountPolicyId));
        return id;
    }

    public Sale findById(Long id) {
        return this.couponMap.get(id);
    }

    public List<Sale> findAll() {
        return new ArrayList<>(couponMap.values());
    }
}
