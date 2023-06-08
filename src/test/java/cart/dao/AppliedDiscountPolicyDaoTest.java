package cart.dao;

import static cart.domain.fixture.MemberFixture.memberWithId;

import cart.dao.entity.AppliedDiscountPolicyEntity;
import cart.dao.entity.OrderRecordEntity;
import cart.dao.entity.PaymentRecordEntity;
import cart.domain.DeliveryPolicies;
import cart.domain.DiscountPolicies;
import cart.domain.Order;
import cart.domain.PaymentRecord;
import cart.domain.fixture.OrderFixture;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class AppliedDiscountPolicyDaoTest {

    private AppliedDiscountPolicyDao appliedDiscountPolicyDao;

    private PaymentRecordDao paymentRecordDao;

    private OrderRecordDao orderRecordDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        appliedDiscountPolicyDao = new AppliedDiscountPolicyDao(jdbcTemplate);
        paymentRecordDao = new PaymentRecordDao(jdbcTemplate);
        orderRecordDao = new OrderRecordDao(jdbcTemplate);
    }

    @Test
    void insert() {
        //given
        Order createdOrder = createOrder();

        DiscountPolicies appliedDiscountPolicy = DiscountPolicies.TEN_PERCENT_DISCOUNT_WHEN_PRICE_IS_UPPER_THAN_FIFTY_THOUSANDS;
        PaymentRecord paymentRecord = new PaymentRecord(createdOrder,
                List.of(appliedDiscountPolicy),
                DeliveryPolicies.DEFAULT);
        Long paymentRecordEntityId = paymentRecordDao.insert(PaymentRecordEntity.from(paymentRecord));

        //when
        Long generatedId = appliedDiscountPolicyDao.insert(paymentRecordEntityId, appliedDiscountPolicy.getId());

        //then
        Assertions.assertThat(appliedDiscountPolicyDao.findByPaymentRecordId(paymentRecordEntityId).stream()
                .map(AppliedDiscountPolicyEntity::getId)).containsOnly(generatedId);
    }

    private Order createOrder() {
        Order orderFixture = OrderFixture.orderByMember(memberWithId);
        Long orderId = orderRecordDao.insert(OrderRecordEntity.from(orderFixture));
        Order createdOrder = new Order(orderId, orderFixture.getMember(), orderFixture.getOrderItems());
        return createdOrder;
    }

    @Test
    void findByPaymentRecordId() {
    }
}