package cart.application;

import cart.domain.Member;
import cart.domain.Orders;
import cart.dto.OrdersRequest;
import cart.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public Long takeOrders(Member member,final OrdersRequest ordersRequest){
        final List<Long> cartIds = ordersRequest.getCartProductIds();
        final int originalPrice = ordersRequest.getOriginalPrice();
        final int discountPrice = ordersRequest.getDiscountPrice();
        final List<Long> coupons = List.of(ordersRequest.getCouponId());
        return ordersRepository.takeOrders(member.getId(),cartIds,originalPrice,discountPrice,coupons);
    }
}
