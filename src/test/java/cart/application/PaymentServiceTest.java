package cart.application;

import cart.domain.Order;
import cart.domain.PaymentRecord;
import cart.domain.fixture.PaymentRecordFixture;
import cart.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cart.domain.fixture.MemberFixture.memberWithId;
import static cart.domain.fixture.OrderFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PaymentServiceTest {

    private final List<Long> CART_ITEM_IDS = List.of(
            1L, 2L, 3L
    );
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        final Order order = orderByMember(memberWithId);
        final Long orderId = this.orderRepository.create(order);
        this.order = this.orderRepository.findById(orderId).orElseThrow();
    }

    @Test
    @DisplayName("Order 객체를 받아서 Payment 생성할 수 있다.")
    void createDraftPaymentRecord() {
        //given
        final PaymentRecord expectedPaymentRecord = PaymentRecordFixture.paymentRecord;
        //when
        final PaymentRecord draftPaymentRecord = this.paymentService.createDraftPaymentRecord(orderWithoutId);
        //then
        assertThat(draftPaymentRecord).usingRecursiveComparison()
                .isEqualTo(expectedPaymentRecord);
    }

    @Test
    @DisplayName("Order 객체를 받아서 PaymentRecord 생성하고 저장한다.")
    void createPaymentRecordAndSave() {
        //given
        final PaymentRecord expectedPaymentRecord = PaymentRecordFixture.paymentRecord;
        //when
        final PaymentRecord actualPaymentRecord = this.paymentService.createPaymentRecordAndSave(this.order);
        //then
        assertThat(actualPaymentRecord).usingRecursiveComparison()
                .isEqualTo(expectedPaymentRecord);
    }

    @Test
    @DisplayName("Order로 PaymentRecord를 찾아 반환한다.")
    void findPaymentRecordByOrder() {
        //given
        final PaymentRecord expectedPaymentRecord = this.paymentService.createPaymentRecordAndSave(this.order);
        //when
        final PaymentRecord actualPaymentRecord = this.paymentService.findByOrder(orderWithId);
        //then
        assertThat(actualPaymentRecord).usingRecursiveComparison()
                .isEqualTo(expectedPaymentRecord);
    }
}