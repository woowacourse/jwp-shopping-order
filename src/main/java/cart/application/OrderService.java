package cart.application;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import cart.application.dto.GetDetailedOrderResponse;
import cart.application.dto.GetOrderResponse;
import cart.application.dto.GetOrdersRequest;
import cart.application.dto.PostOrderRequest;
import cart.domain.Member;

@Service
public class OrderService {

    public List<GetOrderResponse> getOrdersWithPagination(Member member, GetOrdersRequest request) {
        return Collections.emptyList();
    }

    public GetDetailedOrderResponse getOrder(Member member, Long id) {
        return null;
    }

    public Long addOrder(Member member, PostOrderRequest request) {
        return null;
    }
}
