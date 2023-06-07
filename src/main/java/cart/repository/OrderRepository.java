package cart.repository;

import cart.domain.Member;
import cart.domain.Order;
import cart.ui.paging.Page;
import java.util.List;

public interface OrderRepository {

    Long save(final Order order);

    Order findById(final Long id);

    List<Order> findByMember(Member member, Page page);
}
