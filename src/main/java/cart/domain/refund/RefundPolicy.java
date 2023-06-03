package cart.domain.refund;

import cart.domain.order.Order;
import java.time.LocalDateTime;

public interface RefundPolicy {

    boolean isAvailable(final Order order, final LocalDateTime currentTime);
    int calculatePrice(final int price);
}
