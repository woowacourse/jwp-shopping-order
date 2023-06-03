package cart.domain.refund;

import static cart.domain.refund.RefundLimitDate.HALF_REFUND;

import cart.domain.order.Order;
import cart.domain.price.BigDecimalConverter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class HalfRefundPolicy implements RefundPolicy {

    private static final int PERCENTAGE = 100;
    private static final int HALF_REFUND_RATE = 50;
    private static final double DECIMAL_CONVERSION = 0.01;

    @Override
    public boolean isAvailable(final Order order, final LocalDateTime currentTime) {
        return currentTime.isBefore(order.getOrderedAt().plusDays(HALF_REFUND.getDay()));
    }

    @Override
    public BigDecimal calculatePrice(final BigDecimal price) {
        final BigDecimal refundRate = BigDecimalConverter.convert((PERCENTAGE - HALF_REFUND_RATE) * DECIMAL_CONVERSION);
        return price.multiply(refundRate).setScale(0, RoundingMode.DOWN);
    }
}
