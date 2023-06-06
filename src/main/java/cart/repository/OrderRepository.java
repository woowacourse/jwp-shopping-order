package cart.repository;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.repository.dto.OrderAndMainProductDto;
import cart.repository.dto.OrderInfoDto;

import java.util.List;
import java.util.Map;

public interface OrderRepository {

    long save(final Order order);
    List<OrderInfoDto> findByMember(final Member member);
    Map<Long, List<Product>> findProductsByIds(final List<Long> ids);
    Order findById(long id);
}
