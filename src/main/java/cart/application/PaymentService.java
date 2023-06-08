package cart.application;

import cart.domain.Order;
import cart.domain.PaymentRecord;
import cart.exception.PaymentException;
import cart.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentRecord createDraftPaymentRecord(final Order order) {
        return PaymentRecord.from(order);
    }

    public PaymentRecord createPaymentRecordAndSave(final Order order) {
        final PaymentRecord record = this.createDraftPaymentRecord(order);
        this.paymentRepository.create(record);
        return record;
    }

    public PaymentRecord findByOrder(final Order order) {
        return this.paymentRepository.findByOrder(order)
                .orElseThrow(() -> new PaymentException.NotFound(order));
    }
}
