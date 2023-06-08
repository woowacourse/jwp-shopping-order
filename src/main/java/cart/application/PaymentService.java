package cart.application;

import cart.domain.order.Order;
import cart.domain.order.payment.Payment;
import cart.domain.order.payment.PaymentRepository;
import cart.exception.PaymentException;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createDraftPaymentRecord(final Order order) {
        return Payment.from(order);
    }

    public Payment createPaymentRecordAndSave(final Order order) {
        final Payment record = this.createDraftPaymentRecord(order);
        this.paymentRepository.create(record);
        return record;
    }

    public Payment findByOrder(final Order order) {
        return this.paymentRepository.findByOrder(order)
                .orElseThrow(() -> new PaymentException.NotFound(order));
    }
}
