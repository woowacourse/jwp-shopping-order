package cart.repository;

import cart.domain.Order;
import cart.domain.PaymentRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentFakeRepository implements PaymentRepository {
    private final List<PaymentRecord> payments;

    public PaymentFakeRepository() {
        this.payments = new ArrayList<>();
    }

    @Override
    public PaymentRecord save(PaymentRecord paymentRecord) {
        payments.add(paymentRecord);
        return paymentRecord;
    }

    @Override
    public Optional<PaymentRecord> findByOrder(Order order) {
        return payments.stream()
                .filter(payment -> payment.getOrder().getId().equals(order.getId()))
                .findFirst();
    }
}
