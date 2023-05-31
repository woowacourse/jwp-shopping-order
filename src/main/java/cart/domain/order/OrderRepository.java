package cart.domain.order;

import java.util.List;

public interface OrderRepository {

	Long save(final Long memberId, final Order order);

	List<Order> findByMemberId(final Long memberId);

	Order findById(Long id);

	void deleteById(Long id);
}
