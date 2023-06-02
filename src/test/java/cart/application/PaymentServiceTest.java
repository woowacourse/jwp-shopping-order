package cart.application;

import cart.domain.PaymentRecord;
import cart.domain.fixture.PaymentRecordFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static cart.domain.fixture.OrderFixture.orderWithId;
import static cart.domain.fixture.OrderFixture.orderWithoutId;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

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
        final PaymentRecord actualPaymentRecord = this.paymentService.createPaymentRecordAndSave(orderWithoutId);
        //then
        assertThat(actualPaymentRecord).usingRecursiveComparison()
                .isEqualTo(expectedPaymentRecord);
    }

    @Test
    @DisplayName("Order로 PaymentRecord를 찾아 반환한다.")
    void findPaymentRecordByOrder() {
        //given
        final PaymentRecord expectedPaymentRecord = this.paymentService.createPaymentRecordAndSave(orderWithId);
        //when
        final PaymentRecord actualPaymentRecord = this.paymentService.findByOrder(orderWithId);
        //then
        assertThat(actualPaymentRecord).usingRecursiveComparison()
                .isEqualTo(expectedPaymentRecord);
    }
}