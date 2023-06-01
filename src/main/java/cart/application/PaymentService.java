package cart.application;

import cart.domain.DeliveryPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Order;
import cart.domain.Payment;
import cart.domain.PaymentRecord;
import cart.repository.DeliveryPolicyRepository;
import cart.repository.DiscountPolicyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final DiscountPolicyRepository discountPolicyRepository;
    private final DeliveryPolicyRepository deliveryPolicyRepository;

    public PaymentService(DiscountPolicyRepository discountPolicyRepository,
                          DeliveryPolicyRepository deliveryPolicyRepository) {
        this.discountPolicyRepository = discountPolicyRepository;
        this.deliveryPolicyRepository = deliveryPolicyRepository;
    }

    // TODO: 2023/05/31 Custom Exception 만들기
    public PaymentRecord createDraftPaymentRecord(Order order) {
        DiscountPolicy discountPolicy = discountPolicyRepository.findDefault().orElseThrow();
        DeliveryPolicy deliveryPolicy = deliveryPolicyRepository.findDefault().orElseThrow();
        Payment payment = new Payment(List.of(discountPolicy), List.of(deliveryPolicy));

        return payment.createPaymentRecord(order);
    }
}
