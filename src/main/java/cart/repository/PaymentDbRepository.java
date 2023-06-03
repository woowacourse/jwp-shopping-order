package cart.repository;

import cart.dao.AppliedDefaultDeliveryPolicyDao;
import cart.dao.AppliedDefaultDiscountPolicyDao;
import cart.dao.PaymentRecordDao;
import cart.dao.entity.PaymentRecordEntity;
import cart.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaymentDbRepository implements PaymentRepository {

    private final PaymentRecordDao paymentRecordDao;
    private final AppliedDefaultDiscountPolicyDao appliedDefaultDiscountPolicyDao;
    private final AppliedDefaultDeliveryPolicyDao appliedDefaultDeliveryPolicyDao;

    public PaymentDbRepository(final PaymentRecordDao paymentRecordDao,
        final AppliedDefaultDiscountPolicyDao appliedDefaultDiscountPolicyDao,
        final AppliedDefaultDeliveryPolicyDao appliedDefaultDeliveryPolicyDao) {
        this.paymentRecordDao = paymentRecordDao;
        this.appliedDefaultDiscountPolicyDao = appliedDefaultDiscountPolicyDao;
        this.appliedDefaultDeliveryPolicyDao = appliedDefaultDeliveryPolicyDao;
    }

    @Override
    public Long create(final PaymentRecord paymentRecord) {
        final PaymentRecordEntity paymentRecordEntity = PaymentRecordEntity.from(paymentRecord);
        final long paymentRecordId = this.paymentRecordDao.insert(paymentRecordEntity);
        paymentRecord.getPolicyToDiscountAmounts().keySet()
            .forEach(policy -> this.appliedDefaultDiscountPolicyDao.insert(paymentRecordId,
                policy.getId()));
        paymentRecord.getPolicyToDeliveryFees().keySet()
            .forEach(policy -> this.appliedDefaultDeliveryPolicyDao.insert(paymentRecordId,
                policy.getId()));
        return paymentRecordId;
    }

    @Override
    public Optional<PaymentRecord> findByOrder(final Order order) {
        try {
            final PaymentRecordEntity paymentRecordEntity = this.paymentRecordDao.findByOrderId(
                order.getId());
            final List<DiscountPolicy> discountPolicies = this.appliedDefaultDiscountPolicyDao.findByPaymentRecordId(
                paymentRecordEntity.getId());
            final List<DeliveryPolicy> deliveryPolicies = this.appliedDefaultDeliveryPolicyDao.findByPaymentRecordId(
                paymentRecordEntity.getId());
            final Payment payment = new Payment(discountPolicies, deliveryPolicies);
            return Optional.of(payment.createPaymentRecord(order));
        } catch (final Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
