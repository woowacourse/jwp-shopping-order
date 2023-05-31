package cart.application;

import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.dto.order.OrderProductsRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    public long orderProducts(OrderProductsRequest orderProductsRequest) {
        return 0L;
    }

    public List<OrderHistory> getOrderItems(Member member) {
        System.out.println("\n!!!! OrderService.getORderItems() 반환값 변경 필요\n");
        return null;
    }
}
