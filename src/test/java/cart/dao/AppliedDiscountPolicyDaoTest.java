package cart.dao;

import static cart.domain.fixture.MemberFixture.memberWithId;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.AppliedDiscountPolicyEntity;
import cart.dao.entity.OrderRecordEntity;
import cart.dao.entity.PaymentEntity;
import cart.domain.DeliveryPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Order;
import cart.domain.Payment;
import cart.domain.fixture.OrderFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class AppliedDiscountPolicyDaoTest {

    private AppliedDiscountPolicyDao appliedDiscountPolicyDao;

    private PaymentEntityDao paymentEntityDao;

    private OrderRecordDao orderRecordDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        appliedDiscountPolicyDao = new AppliedDiscountPolicyDao(jdbcTemplate);
        paymentEntityDao = new PaymentEntityDao(jdbcTemplate);
        orderRecordDao = new OrderRecordDao(jdbcTemplate);
    }

    @Test
    void insert() {
        //given
        Order createdOrder = createOrder();

        DiscountPolicy appliedDiscountPolicy = DiscountPolicy.TEN_PERCENT_DISCOUNT_WHEN_PRICE_IS_UPPER_THAN_FIFTY_THOUSANDS;
        Payment payment = new Payment(createdOrder,
                List.of(appliedDiscountPolicy),
                DeliveryPolicy.DEFAULT);
        Long paymentEntityId = paymentEntityDao.insert(PaymentEntity.from(payment));

        //when
        //then
        Long generatedId = appliedDiscountPolicyDao.insert(paymentEntityId, appliedDiscountPolicy.getId());
        assertThat(generatedId).isNotNull();
    }

    private Order createOrder() {
        Order orderFixture = OrderFixture.orderByMember(memberWithId);
        Long orderId = orderRecordDao.insert(OrderRecordEntity.from(orderFixture));
        Order createdOrder = new Order(orderId, orderFixture.getMember(), orderFixture.getOrderItems());
        return createdOrder;
    }

    @Test
    void findByPaymentRecordId() {
        //given
        Order createdOrder = createOrder();

        DiscountPolicy appliedDiscountPolicy = DiscountPolicy.TEN_PERCENT_DISCOUNT_WHEN_PRICE_IS_UPPER_THAN_FIFTY_THOUSANDS;
        Payment payment = new Payment(createdOrder,
                List.of(appliedDiscountPolicy),
                DeliveryPolicy.DEFAULT);
        Long paymentEntityId = paymentEntityDao.insert(PaymentEntity.from(payment));
        Long generatedId = appliedDiscountPolicyDao.insert(paymentEntityId, appliedDiscountPolicy.getId());

        //when
        assertThat(appliedDiscountPolicyDao.findByPaymentEntityId(paymentEntityId).stream()
                .map(AppliedDiscountPolicyEntity::getId))
                .containsOnly(generatedId);
    }
}