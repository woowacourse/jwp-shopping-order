package cart.repository;

import cart.domain.Order;
import cart.domain.PaymentRecord;
import java.util.Optional;

public interface PaymentRepository {
//    Long save(Payment payment, Long orderId);

    PaymentRecord save(PaymentRecord paymentRecord);

    Optional<PaymentRecord> findByOrder(Order order);

}
