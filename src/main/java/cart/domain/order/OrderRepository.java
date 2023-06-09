package cart.domain.order;

import cart.domain.member.Member;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Long create(Order order);

    List<Order> findByMember(Member member);

    Optional<Order> findById(Long id);

}
