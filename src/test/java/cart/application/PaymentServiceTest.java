package cart.application;

import static cart.domain.fixture.MemberFixture.memberWithId;
import static cart.domain.fixture.OrderFixture.orderByMember;
import static cart.domain.fixture.OrderFixture.orderWithoutId;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.fixture.PaymentRecordFixture;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.domain.order.payment.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PaymentServiceTest {

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
        final Payment expectedPayment = PaymentRecordFixture.PAYMENT;
        //when
        final Payment draftPayment = this.paymentService.createDraftPaymentRecord(orderWithoutId);
        //then
        assertThat(draftPayment.getOriginalOrderPrice()).isEqualTo(expectedPayment.getOriginalOrderPrice());
        assertThat(draftPayment.calculateDiscountedPrice()).isEqualTo(
                expectedPayment.calculateDiscountedPrice());
        assertThat(draftPayment.calculateDeliveryFee()).isEqualTo(expectedPayment.calculateDeliveryFee());
        assertThat(draftPayment.calculateFinalPrice()).isEqualTo(expectedPayment.calculateFinalPrice());
    }

    @Test
    @DisplayName("Order 객체를 받아서 PaymentRecord 생성하고 저장한다.")
    void createPaymentRecordAndSave() {
        //given
        final Payment expectedPayment = PaymentRecordFixture.PAYMENT;
        //when
        final Payment actualPayment = this.paymentService.createPaymentRecordAndSave(this.order);
        //then
        assertThat(actualPayment.getOriginalOrderPrice()).isEqualTo(
                expectedPayment.getOriginalOrderPrice());
        assertThat(actualPayment.calculateDiscountedPrice()).isEqualTo(
                expectedPayment.calculateDiscountedPrice());
        assertThat(actualPayment.calculateDeliveryFee()).isEqualTo(expectedPayment.calculateDeliveryFee());
        assertThat(actualPayment.calculateFinalPrice()).isEqualTo(expectedPayment.calculateFinalPrice());
    }

    @Test
    @DisplayName("Order로 PaymentRecord를 찾아 반환한다.")
    void findPaymentRecordByOrder() {
        //given
        final Payment expectedPayment = this.paymentService.createPaymentRecordAndSave(this.order);
        //when
        final Payment actualPayment = this.paymentService.findByOrder(this.order);
        //then
        assertThat(actualPayment.getOriginalOrderPrice()).isEqualTo(
                expectedPayment.getOriginalOrderPrice());
        assertThat(actualPayment.calculateDiscountedPrice()).isEqualTo(
                expectedPayment.calculateDiscountedPrice());
        assertThat(actualPayment.calculateDeliveryFee()).isEqualTo(expectedPayment.calculateDeliveryFee());
        assertThat(actualPayment.calculateFinalPrice()).isEqualTo(expectedPayment.calculateFinalPrice());
    }
}