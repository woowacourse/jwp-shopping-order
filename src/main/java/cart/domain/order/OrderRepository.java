package cart.domain.order;

import java.util.List;

public interface OrderRepository {

    Long save(final Order order);

    Long saveWithCoupon(final Order order);

    Long countByMemberId(final Long memberId);

    OrderWithId getById(final Long id);

    List<OrderWithId> findByMemberName(final String memberName);
}
