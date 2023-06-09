package cart.domain.pointRewardPolicy;

import cart.domain.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RatePointRewardPolicy implements PointRewardPolicy {

    private static final Long RATE = 10L;

    @Override
    public BigDecimal calculate(final Payment payment) {
        final BigDecimal paymentAmount = payment.getPayment();
        return BigDecimal.valueOf(paymentAmount.longValue() / RATE);
    }
}
