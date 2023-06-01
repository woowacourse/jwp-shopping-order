package cart.application;

import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.dto.order.OrderDetailResponse;
import cart.dto.order.OrderProductsRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    public long orderProducts(Member member, OrderProductsRequest orderProductsRequest) {
        return 0L;
    }

    public List<OrderHistory> getOrderItems(Member member) {
        System.out.println("\n!!!! OrderService.getOrderItems() 반환값 변경 필요\n");
        return null;
    }

    public List<OrderDetailResponse> getOrderItemDetailById(Member member, long id) {
        System.out.println("\n!!!! OrderService.getOrderItemDetailById() 반환값 변경 필요\n");
        return null;
    }
}
