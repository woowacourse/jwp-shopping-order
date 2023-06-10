package cart.dao;

import static cart.fixture.MemberFixture.MEMBER_1;
import static cart.fixture.OrderFixture.ORDER_1;
import static cart.fixture.PaymentFixture.PAYMENT_1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import cart.entity.PaymentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class PaymentDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PaymentDao paymentDao;
    private OrderDao orderDao;
    private MemberDao memberDao;
    private Long orderId;
    private Long memberId;

    @BeforeEach
    void setUp() {
        paymentDao = new PaymentDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);

        orderId = orderDao.save(ORDER_1);
        memberId = memberDao.addMember(MEMBER_1);
    }

    @Test
    @DisplayName("결제 정보를 저장한다.")
    void save() {
        // when
        Long paymentId = paymentDao.save(PAYMENT_1, orderId, memberId);

        // then
        assertThat(paymentId).isNotNull();
    }

    @Test
    @DisplayName("주문 ID로 결제 정보를 조회한다.")
    void findByOrderId() {
        // given
        Long id = paymentDao.save(PAYMENT_1, orderId, memberId);

        // when
        PaymentEntity paymentEntity = paymentDao.findByOrderId(orderId);

        // then
        assertThat(paymentEntity)
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(new PaymentEntity(id,
                        memberId,
                        PAYMENT_1.getOriginalPrice().getValue(),
                        PAYMENT_1.getDiscountPrice().getValue(),
                        PAYMENT_1.getFinalPrice().getValue(),
                        null));
    }
}
