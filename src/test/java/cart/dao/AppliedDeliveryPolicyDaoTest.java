package cart.dao;

import static cart.domain.fixture.MemberFixture.memberWithId;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.OrderRecordEntity;
import cart.dao.entity.PaymentEntity;
import cart.domain.DeliveryPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Order;
import cart.domain.Payment;
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

    private PaymentEntityDao paymentEntityDao;

    private OrderRecordDao orderRecordDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        appliedDeliveryPolicyDao = new AppliedDeliveryPolicyDao(jdbcTemplate);
        paymentEntityDao = new PaymentEntityDao(jdbcTemplate);
        orderRecordDao = new OrderRecordDao(jdbcTemplate);
    }

    @Test
    @DisplayName("삽입하기 테스트")
    void insert() {
        //given
        Order createdOrder = createOrder();

        DeliveryPolicy appliedDeliveryPolicy = DeliveryPolicy.DEFAULT;
        Payment payment = new Payment(createdOrder,
                List.of(DiscountPolicy.TEN_PERCENT_DISCOUNT_WHEN_PRICE_IS_UPPER_THAN_FIFTY_THOUSANDS),
                appliedDeliveryPolicy);
        Long paymentEntityId = paymentEntityDao.insert(PaymentEntity.from(payment));

        //when
        //then
        Long generatedId = appliedDeliveryPolicyDao.insert(paymentEntityId, appliedDeliveryPolicy.getId());
        assertThat(generatedId).isNotNull();
    }

    private Order createOrder() {
        Order orderFixture = OrderFixture.orderByMember(memberWithId);
        Long orderId = orderRecordDao.insert(OrderRecordEntity.from(orderFixture));
        Order createdOrder = new Order(orderId, orderFixture.getMember(), orderFixture.getOrderItems());
        return createdOrder;
    }
}