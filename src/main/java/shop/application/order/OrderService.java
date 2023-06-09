package shop.application.order;

import shop.application.order.dto.OrderCreationDto;
import shop.application.order.dto.OrderDetailDto;
import shop.application.order.dto.OrderDto;
import shop.domain.member.Member;

import java.util.List;

public interface OrderService {
    Long order(Member member, OrderCreationDto orderCreationDto);

    List<OrderDto> getAllOrderHistoryOfMember(Member member);

    OrderDetailDto getOrderDetailsOfMember(Member member, Long orderId);
}
