package cart.application;

import static cart.domain.fixture.OrderFixture.orderWithId;
import static cart.domain.fixture.OrderFixture.orderWithoutId;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.PaymentRecord;
import cart.domain.fixture.PaymentRecordFixture;
import cart.repository.DeliveryPolicyFakeRepository;
import cart.repository.DeliveryPolicyRepository;
import cart.repository.DiscountPolicyFakeRepository;
import cart.repository.DiscountPolicyRepository;
import cart.repository.PaymentFakeRepository;
import cart.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentServiceTest {

    private DiscountPolicyRepository discountPolicyRepository;
    private DeliveryPolicyRepository deliveryPolicyRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        discountPolicyRepository = new DiscountPolicyFakeRepository();
        deliveryPolicyRepository = new DeliveryPolicyFakeRepository();
        PaymentRepository paymentRepository = new PaymentFakeRepository();
        paymentService = new PaymentService(discountPolicyRepository, deliveryPolicyRepository, paymentRepository);
    }

    @Test
    @DisplayName("Order 객체를 받아서 Payment 생성할 수 있다.")
    void createDraftPaymentRecord() {
        //given
        PaymentRecord expectedPaymentRecord = PaymentRecordFixture.paymentRecord;
        //when
        PaymentRecord draftPaymentRecord = paymentService.createDraftPaymentRecord(orderWithoutId);
        //then
        assertThat(draftPaymentRecord).usingRecursiveComparison()
                .isEqualTo(expectedPaymentRecord);
    }

    @Test
    @DisplayName("Order 객체를 받아서 PaymentRecord 생성하고 저장한다.")
    void createPaymentRecordAndSave() {
        //given
        PaymentRecord expectedPaymentRecord = PaymentRecordFixture.paymentRecord;
        //when
        PaymentRecord actualPaymentRecord = paymentService.createPaymentRecordAndSave(orderWithoutId);
        //then
        assertThat(actualPaymentRecord).usingRecursiveComparison()
                .isEqualTo(expectedPaymentRecord);
    }

    @Test
    @DisplayName("Order로 PaymentRecord를 찾아 반환한다.")
    void findPaymentRecordByOrder() {
        //given
        PaymentRecord expectedPaymentRecord = paymentService.createPaymentRecordAndSave(orderWithId);
        //when
        PaymentRecord actualPaymentRecord = paymentService.findByOrder(orderWithId);
        //then
        assertThat(actualPaymentRecord).usingRecursiveComparison()
                .isEqualTo(expectedPaymentRecord);
    }
}