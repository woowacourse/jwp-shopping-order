package cart.domain.order;

import java.util.List;

public interface OrderRepository {

	Long save(Long memberId, Order order);

	List<Order> findByMemberId(Long memberId);

	Order findById(Long id);

	void updateStatus(Order order);

	void deleteById(Long id);
}
