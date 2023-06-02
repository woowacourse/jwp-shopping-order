package cart.repository;

import cart.domain.Member;
import cart.domain.Order;
import cart.repository.dto.OrderAndMainProductDto;

import java.util.List;

public interface OrderRepository {

    long save(final Order order);
    List<OrderAndMainProductDto> findByMember(final Member member);
    Order findById(long id);
}
