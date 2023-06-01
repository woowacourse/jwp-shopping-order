package cart.repository;

import cart.domain.Order;
import cart.domain.Payment;
import java.util.Optional;

public interface PaymentRepository {
    Long save(Payment payment, Long orderId);

    Optional<Payment> findByOrder(Order order);

}
