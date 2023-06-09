package cart.order.application;

import cart.order.domain.Payment;
import java.util.Optional;

public interface PaymentRepository {
    void save(Long orderId, Payment payment);
    Optional<Payment> findByOrderId(Long orderId);
}
