package cart.application;

import cart.domain.Member;
import cart.dto.request.OrderRequestDto;
import cart.dto.response.OrderResponseDto;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public OrderResponseDto order(final Member member, final OrderRequestDto orderRequestDto) {
        return new OrderResponseDto(1L);
    }
}
