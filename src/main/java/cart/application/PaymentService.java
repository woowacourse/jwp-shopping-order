package cart.application;

import cart.domain.DeliveryPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Order;
import cart.domain.Payment;
import cart.domain.PaymentRecord;
import cart.exception.PaymentException;
import cart.repository.DeliveryPolicyRepository;
import cart.repository.DiscountPolicyRepository;
import cart.repository.PaymentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final DiscountPolicyRepository discountPolicyRepository;
    private final DeliveryPolicyRepository deliveryPolicyRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(DiscountPolicyRepository discountPolicyRepository,
                          DeliveryPolicyRepository deliveryPolicyRepository, PaymentRepository paymentRepository) {
        this.discountPolicyRepository = discountPolicyRepository;
        this.deliveryPolicyRepository = deliveryPolicyRepository;
        this.paymentRepository = paymentRepository;
    }

    public PaymentRecord createDraftPaymentRecord(Order order) {
        List<DiscountPolicy> discountPolicies = discountPolicyRepository.findDefault();
        List<DeliveryPolicy> deliveryPolicies = deliveryPolicyRepository.findDefault();

        Payment payment = new Payment(discountPolicies, deliveryPolicies);

        return payment.createPaymentRecord(order);
    }

    public PaymentRecord createPaymentRecordAndSave(Order order) {
        PaymentRecord record = createDraftPaymentRecord(order);
        return paymentRepository.save(record);
    }

    public PaymentRecord findByOrder(Order order) {
        return paymentRepository.findByOrder(order)
                .orElseThrow(() -> new PaymentException.NotFound(order));
    }
}
