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
    PaymentRecordDao paymentRecordDao;
    AppliedDefaultDiscountPolicyDao appliedDefaultDiscountPolicyDao;
    AppliedDefaultDeliveryPolicyDao appliedDefaultDeliveryPolicyDao;

    @Override
    public Long create(final PaymentRecord paymentRecord) {
        final PaymentRecordEntity paymentRecordEntity = PaymentRecordEntity.from(paymentRecord);
        final long paymentRecordId = this.paymentRecordDao.insert(paymentRecordEntity);
        paymentRecord.getPolicyToDiscountAmounts().keySet()
                .forEach(policy -> this.appliedDefaultDiscountPolicyDao.insert(paymentRecordId, policy.getId()));
        paymentRecord.getPolicyToDeliveryFees().keySet()
                .forEach(policy -> this.appliedDefaultDeliveryPolicyDao.insert(paymentRecordId, policy.getId()));
        return paymentRecordId;
    }

    @Override
    public Optional<PaymentRecord> findByOrder(final Order order) {
        try {
            final PaymentRecordEntity paymentRecordEntity = this.paymentRecordDao.findByOrderId(order.getId());
            final DiscountPolicy discountPolicy = this.appliedDefaultDiscountPolicyDao.findByPaymentRecordId(paymentRecordEntity.getId());
            final DeliveryPolicy deliveryPolicy = this.appliedDefaultDeliveryPolicyDao.findByPaymentRecordId(paymentRecordEntity.getId());
            final Payment payment = new Payment(List.of(discountPolicy), List.of(deliveryPolicy));
            return Optional.of(payment.createPaymentRecord(order));
        } catch (final Exception e) {
            return Optional.empty();
        }
    }
}
