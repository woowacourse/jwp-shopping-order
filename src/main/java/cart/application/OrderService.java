package cart.application;

import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    public Long orderItems(Long id, OrderRequest orderRequest) {
        return null;
    }

    public List<OrderResponse> findOrdersByMember(Member member) {
        return null;
    }
}
