package cart.domain.refund;

import static cart.domain.refund.RefundLimitDate.FULL_REFUND;

import cart.domain.order.Order;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class FullRefundPolicy implements RefundPolicy {

    @Override
    public boolean isAvailable(final Order order, final LocalDateTime currentTime) {
        return currentTime.isBefore(order.getOrderedAt().plusDays(FULL_REFUND.getDay()));
    }

    @Override
    public int calculatePrice(final int price) {
        return price;
    }
}
