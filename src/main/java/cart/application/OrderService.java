package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderListResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    public Long add(final Member member, final OrderRequest orderRequest) {
        return 1L;
    }

    public OrderResponse findById(final Member member, final Long id) {
        final Order order = new Order(1L, List.of(), 0, 0, 0, 0, "");
        return OrderResponse.of(order);
    }

    public OrderListResponse findPageByIndex(final Member member, final Long idx) {
        return OrderListResponse.of(List.of());
    }
}
