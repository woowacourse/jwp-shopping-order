package cart.repository;

import cart.dao.AppliedDeliveryPolicyDao;
import cart.dao.AppliedDiscountPolicyDao;
import cart.dao.PaymentEntityDao;
import cart.dao.entity.AppliedDeliveryPolicyEntity;
import cart.dao.entity.PaymentEntity;
import cart.domain.DeliveryPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Order;
import cart.domain.Payment;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDbRepository implements PaymentRepository {
    private final PaymentEntityDao paymentEntityDao;
    private final AppliedDiscountPolicyDao appliedDiscountPolicyDao;
    private final AppliedDeliveryPolicyDao appliedDeliveryPolicyDao;

    public PaymentDbRepository(final PaymentEntityDao paymentEntityDao,
                               final AppliedDiscountPolicyDao appliedDiscountPolicyDao,
                               final AppliedDeliveryPolicyDao appliedDeliveryPolicyDao) {
        this.paymentEntityDao = paymentEntityDao;
        this.appliedDiscountPolicyDao = appliedDiscountPolicyDao;
        this.appliedDeliveryPolicyDao = appliedDeliveryPolicyDao;
    }

    @Override
    public Long create(final Payment payment) {
        final PaymentEntity paymentEntity = PaymentEntity.from(payment);
        final long paymentEntityId = this.paymentEntityDao.insert(paymentEntity);
        payment.getDiscountPolicies()
                .forEach(policy -> this.appliedDiscountPolicyDao.insert(paymentEntityId, policy.getId()));
        DeliveryPolicy deliveryPolicy = payment.getDeliveryPolicy();
        this.appliedDeliveryPolicyDao.insert(paymentEntityId, deliveryPolicy.getId());
        return paymentEntityId;
    }

    @Override
    public Optional<Payment> findByOrder(final Order order) {
        try {
            final PaymentEntity paymentEntity = this.paymentEntityDao.findByOrderId(order.getId());
            final List<DiscountPolicy> appliedDiscountPolicies = findAppliedDiscountPolicies(paymentEntity);
            final DeliveryPolicy deliveryPolicy = findAppliedDeliveryPolicy(paymentEntity);

            final Payment payment = new Payment(order, appliedDiscountPolicies, deliveryPolicy);
            return Optional.of(payment);
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    private DeliveryPolicy findAppliedDeliveryPolicy(PaymentEntity paymentEntity) {
        AppliedDeliveryPolicyEntity appliedDeliveryPolicy = appliedDeliveryPolicyDao.findByPaymentRecordId(
                paymentEntity.getId());
        DeliveryPolicy deliveryPolicy = DeliveryPolicy.findById(appliedDeliveryPolicy.getDeliveryPolicyId());
        return deliveryPolicy;
    }

    private List<DiscountPolicy> findAppliedDiscountPolicies(PaymentEntity paymentEntity) {
        return this.appliedDiscountPolicyDao
                .findByPaymentEntityId(paymentEntity.getId())
                .stream().map(entity -> DiscountPolicy.findById(entity.getId()))
                .collect(Collectors.toList());
    }
}
