package cart.domain.refund;

import cart.domain.order.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RefundPolicy {

    boolean isAvailable(final Order order, final LocalDateTime currentTime);

    BigDecimal calculatePrice(final BigDecimal price);
}
