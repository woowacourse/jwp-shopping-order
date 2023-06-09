package cart.repository;

import cart.dao.AppliedDefaultDeliveryPolicyDao;
import cart.dao.AppliedDefaultDiscountPolicyDao;
import cart.dao.PaymentRecordDao;
import cart.dao.entity.PaymentRecordEntity;
import cart.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class PaymentDbRepository implements PaymentRepository {
    private final PaymentRecordDao paymentRecordDao;
    private final AppliedDefaultDiscountPolicyDao appliedDefaultDiscountPolicyDao;
    private final AppliedDefaultDeliveryPolicyDao appliedDefaultDeliveryPolicyDao;
    private final List<Function<Long, DiscountPolicy>> applicableDiscountPolicies;
    private final List<Function<Long, DeliveryPolicy>> applicableDeliveryPolicies;

    public PaymentDbRepository(final PaymentRecordDao paymentRecordDao, final AppliedDefaultDiscountPolicyDao appliedDefaultDiscountPolicyDao, final AppliedDefaultDeliveryPolicyDao appliedDefaultDeliveryPolicyDao) {
        this.paymentRecordDao = paymentRecordDao;
        this.appliedDefaultDiscountPolicyDao = appliedDefaultDiscountPolicyDao;
        this.appliedDefaultDeliveryPolicyDao = appliedDefaultDeliveryPolicyDao;
        this.applicableDiscountPolicies = List.of(this.appliedDefaultDiscountPolicyDao::findByPaymentRecordId);
        this.applicableDeliveryPolicies = List.of(this.appliedDefaultDeliveryPolicyDao::findByPaymentRecordId);
    }

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
            final List<DiscountPolicy> appliedDiscountPolicies = this.getPolicies(this.applicableDiscountPolicies, paymentRecordEntity);
            final List<DeliveryPolicy> appliedDeliveryPolicies = this.getPolicies(this.applicableDeliveryPolicies, paymentRecordEntity);
            final Payment payment = new Payment(appliedDiscountPolicies, appliedDeliveryPolicies);
            return Optional.of(payment.createPaymentRecord(order));
        } catch (final Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private <T> List<T> getPolicies(final List<Function<Long, T>> functions, final PaymentRecordEntity paymentRecordEntity) {
        return functions.stream()
                .map(function -> this.wrapFunction(function, paymentRecordEntity))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableList());
    }

    private <T> Optional<T> wrapFunction(final Function<Long, T> function, final PaymentRecordEntity paymentRecordEntity) {
        try {
            return Optional.of(function.apply(paymentRecordEntity.getId()));
        } catch (final Exception e) {
            return Optional.empty();
        }
    }
}
