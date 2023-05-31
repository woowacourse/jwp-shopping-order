package cart.domain.pointRewardPolicy;

import cart.domain.Payment;

import java.math.BigDecimal;

public interface PointRewardPolicy {

    BigDecimal calculate(Payment payment);
}
