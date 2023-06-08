package cart.domain.order.payment;

import cart.domain.order.Order;
import java.util.Optional;

public interface PaymentRepository {
//    Long save(Payment payment, Long orderId);

    Long create(Payment payment);

    Optional<Payment> findByOrder(Order order);

}
