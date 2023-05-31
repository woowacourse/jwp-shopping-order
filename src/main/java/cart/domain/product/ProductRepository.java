package cart.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

	Long save(final Product product);

	List<Product> findAll();

	Optional<Product> findById(final Long id);

	void update(final Product product);

	void deleteById(final Long id);

}
