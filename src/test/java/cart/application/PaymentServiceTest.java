package cart.application;

import static cart.domain.fixture.OrderFixture.order;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.PaymentRecord;
import cart.domain.fixture.PaymentRecordFixture;
import cart.repository.DeliveryPolicyFakeRepository;
import cart.repository.DeliveryPolicyRepository;
import cart.repository.DiscountPolicyFakeRepository;
import cart.repository.DiscountPolicyRepository;
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
        paymentService = new PaymentService(discountPolicyRepository, deliveryPolicyRepository);
    }

    @Test
    @DisplayName("Order 객체를 받아서 Payment 생성할 수 있다.")
    void createDraftPaymentRecord() {
        //given
        PaymentRecord expectedPaymentRecord = PaymentRecordFixture.paymentRecord;
        //when
        PaymentRecord draftPaymentRecord = paymentService.createDraftPaymentRecord(order);
        //then
        assertThat(draftPaymentRecord).usingRecursiveComparison()
                .isEqualTo(expectedPaymentRecord);

    }
}