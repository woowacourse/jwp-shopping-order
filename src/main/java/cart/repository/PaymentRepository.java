package cart.repository;

import cart.domain.Order;
import cart.domain.PaymentRecord;

import java.util.Optional;

public interface PaymentRepository {
//    Long save(Payment payment, Long orderId);

    Long create(PaymentRecord paymentRecord);

    Optional<PaymentRecord> findByOrder(Order order);

}
