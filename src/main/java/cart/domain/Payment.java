package cart.domain;

import java.math.BigDecimal;

public class Payment {

    private final BigDecimal payment;

    public Payment(final BigDecimal payment) {
        validateMinimum(payment);
        this.payment = payment;
    }

    private void validateMinimum(BigDecimal payment){
        if (payment.longValue() < BigDecimal.ZERO.longValue()) {
            throw new IllegalArgumentException("지불금액은 0 이상이어야 합니다");
        }
    }

    public BigDecimal getPayment() {
        return payment;
    }
}
