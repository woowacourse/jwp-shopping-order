package cart.dao;

import static cart.domain.fixture.MemberFixture.memberWithId;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.OrderRecordEntity;
import cart.dao.entity.PaymentRecordEntity;
import cart.domain.DeliveryPolicies;
import cart.domain.DiscountPolicies;
import cart.domain.Order;
import cart.domain.PaymentRecord;
import cart.domain.fixture.OrderFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class AppliedDeliveryPolicyDaoTest {

    private AppliedDeliveryPolicyDao appliedDeliveryPolicyDao;

    private PaymentRecordDao paymentRecordDao;

    private OrderRecordDao orderRecordDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        appliedDeliveryPolicyDao = new AppliedDeliveryPolicyDao(jdbcTemplate);
        paymentRecordDao = new PaymentRecordDao(jdbcTemplate);
        orderRecordDao = new OrderRecordDao(jdbcTemplate);
    }

    @Test
    @DisplayName("삽입하기 테스트")
    void insert() {
        //given
        Order createdOrder = createOrder();

        DeliveryPolicies appliedDeliveryPolicy = DeliveryPolicies.DEFAULT;
        PaymentRecord paymentRecord = new PaymentRecord(createdOrder,
                List.of(DiscountPolicies.TEN_PERCENT_DISCOUNT_WHEN_PRICE_IS_UPPER_THAN_FIFTY_THOUSANDS),
                appliedDeliveryPolicy);
        Long paymentRecordEntityId = paymentRecordDao.insert(PaymentRecordEntity.from(paymentRecord));

        //when
        //then
        Long generatedId = appliedDeliveryPolicyDao.insert(paymentRecordEntityId, appliedDeliveryPolicy.getId());
        assertThat(generatedId).isNotNull();
    }

    private Order createOrder() {
        Order orderFixture = OrderFixture.orderByMember(memberWithId);
        Long orderId = orderRecordDao.insert(OrderRecordEntity.from(orderFixture));
        Order createdOrder = new Order(orderId, orderFixture.getMember(), orderFixture.getOrderItems());
        return createdOrder;
    }
}