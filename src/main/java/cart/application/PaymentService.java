package cart.application;

import cart.domain.Order;
import cart.domain.Payment;
import cart.exception.PaymentException;
import cart.repository.PaymentRepository;
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
