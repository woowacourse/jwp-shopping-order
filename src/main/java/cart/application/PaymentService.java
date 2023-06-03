package cart.application;

import cart.domain.*;
import cart.exception.PaymentException;
import cart.repository.DeliveryPolicyRepository;
import cart.repository.DiscountPolicyRepository;
import cart.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final DiscountPolicyRepository discountPolicyRepository;
    private final DeliveryPolicyRepository deliveryPolicyRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(final DiscountPolicyRepository discountPolicyRepository,
                          final DeliveryPolicyRepository deliveryPolicyRepository, final PaymentRepository paymentRepository) {
        this.discountPolicyRepository = discountPolicyRepository;
        this.deliveryPolicyRepository = deliveryPolicyRepository;
        this.paymentRepository = paymentRepository;
    }

    public PaymentRecord createDraftPaymentRecord(final Order order) {
        final List<DiscountPolicy> discountPolicies = this.discountPolicyRepository.findDefault();
        final List<DeliveryPolicy> deliveryPolicies = this.deliveryPolicyRepository.findDefault();
        final Payment payment = new Payment(discountPolicies, deliveryPolicies);
        return payment.createPaymentRecord(order);
    }

    public PaymentRecord createPaymentRecordAndSave(final Order order) {
        final PaymentRecord record = this.createDraftPaymentRecord(order);
        final Long id = this.paymentRepository.create(record);
        return record;
    }

    public PaymentRecord findByOrder(final Order order) {
        return this.paymentRepository.findByOrder(order)
                .orElseThrow(() -> new PaymentException.NotFound(order));
    }
}
