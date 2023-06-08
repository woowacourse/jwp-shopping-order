package cart.repository;

import cart.dao.AppliedDeliveryPolicyDao;
import cart.dao.AppliedDiscountPolicyDao;
import cart.dao.PaymentRecordDao;
import cart.dao.entity.AppliedDeliveryPolicyEntity;
import cart.dao.entity.PaymentRecordEntity;
import cart.domain.DeliveryPolicies;
import cart.domain.DiscountPolicies;
import cart.domain.Order;
import cart.domain.PaymentRecord;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDbRepository implements PaymentRepository {
    private final PaymentRecordDao paymentRecordDao;
    private final AppliedDiscountPolicyDao appliedDiscountPolicyDao;
    private final AppliedDeliveryPolicyDao appliedDeliveryPolicyDao;

    public PaymentDbRepository(final PaymentRecordDao paymentRecordDao,
                               final AppliedDiscountPolicyDao appliedDiscountPolicyDao,
                               final AppliedDeliveryPolicyDao appliedDeliveryPolicyDao) {
        this.paymentRecordDao = paymentRecordDao;
        this.appliedDiscountPolicyDao = appliedDiscountPolicyDao;
        this.appliedDeliveryPolicyDao = appliedDeliveryPolicyDao;
    }

    @Override
    public Long create(final PaymentRecord paymentRecord) {
        final PaymentRecordEntity paymentRecordEntity = PaymentRecordEntity.from(paymentRecord);
        final long paymentRecordId = this.paymentRecordDao.insert(paymentRecordEntity);
        paymentRecord.getDiscountPolicies()
                .forEach(policy -> this.appliedDiscountPolicyDao.insert(paymentRecordId, policy.getId()));
        DeliveryPolicies deliveryPolicy = paymentRecord.getDeliveryPolicy();
        this.appliedDeliveryPolicyDao.insert(paymentRecordId, deliveryPolicy.getId());
        return paymentRecordId;
    }

    @Override
    public Optional<PaymentRecord> findByOrder(final Order order) {
        try {
            final PaymentRecordEntity paymentRecordEntity = this.paymentRecordDao.findByOrderId(order.getId());
            final List<DiscountPolicies> appliedDiscountPolicies = findAppliedDiscountPolicies(paymentRecordEntity);
            final DeliveryPolicies deliveryPolicy = findAppliedDeliveryPolicy(paymentRecordEntity);

            final PaymentRecord paymentRecord = new PaymentRecord(order, appliedDiscountPolicies, deliveryPolicy);
            return Optional.of(paymentRecord);
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    private DeliveryPolicies findAppliedDeliveryPolicy(PaymentRecordEntity paymentRecordEntity) {
        AppliedDeliveryPolicyEntity appliedDeliveryPolicy = appliedDeliveryPolicyDao.findByPaymentRecordId(
                paymentRecordEntity.getId());
        DeliveryPolicies deliveryPolicy = DeliveryPolicies.findById(appliedDeliveryPolicy.getDeliveryPolicyId());
        return deliveryPolicy;
    }

    private List<DiscountPolicies> findAppliedDiscountPolicies(PaymentRecordEntity paymentRecordEntity) {
        return this.appliedDiscountPolicyDao
                .findByPaymentRecordId(paymentRecordEntity.getId())
                .stream().map(entity -> DiscountPolicies.findById(entity.getId()))
                .collect(Collectors.toList());
    }
}
