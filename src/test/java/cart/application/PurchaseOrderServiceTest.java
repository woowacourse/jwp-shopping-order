package cart.application;

import cart.dao.PurchaseOrderDao;
import cart.dao.PurchaseOrderItemDao;
import cart.domain.Member;
import cart.dto.purchaseorder.PurchaseOrderItemInfoResponse;
import cart.dto.purchaseorder.response.PurchaseOrderPageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static cart.TestFeatures.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

@JdbcTest
class PurchaseOrderServiceTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    private PurchaseOrderDao purchaseOrderDao;
    private PurchaseOrderItemDao purchaseOrderItemDao;
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        purchaseOrderItemDao = new PurchaseOrderItemDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
        purchaseOrderDao = new PurchaseOrderDao(namedParameterJdbcTemplate, dataSource, purchaseOrderItemDao);
        purchaseOrderService = new PurchaseOrderService(purchaseOrderDao, purchaseOrderItemDao);
    }

    @DisplayName("특정 회원이 원하는 페이지에 대한 주문 목록을 조회할 수 있다")
    @Test
    void getAllByMemberId() {
        // given
        List<PurchaseOrderItemInfoResponse> expectInfoResponses = List.of(
                new PurchaseOrderItemInfoResponse(회원1_주문1.getId(), 회원1_주문1.getPayment(), 회원1_주문1.getOrderAt(),
                        주문1_상품1.getProduct().getName(), 주문1_상품1.getProduct().getImageUrl(), 2),
                new PurchaseOrderItemInfoResponse(회원1_주문2.getId(), 회원1_주문2.getPayment(), 회원1_주문2.getOrderAt(),
                        주문2_상품1.getProduct().getName(), 주문2_상품1.getProduct().getImageUrl(), 2),
                new PurchaseOrderItemInfoResponse(회원1_주문3.getId(), 회원1_주문3.getPayment(), 회원1_주문3.getOrderAt(),
                        주문3_상품1.getProduct().getName(), 주문3_상품1.getProduct().getImageUrl(), 2)
        );

        Member member = 회원1;

        // when
        PurchaseOrderPageResponse purchaseOrderPageResponse = purchaseOrderService.getAllByMemberId(member, 1);

        // then
        assertThat(purchaseOrderPageResponse).isEqualTo(
                new PurchaseOrderPageResponse(1, 1, 3, expectInfoResponses)
        );
    }
}
