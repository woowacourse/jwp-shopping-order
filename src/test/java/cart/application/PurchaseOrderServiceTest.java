package cart.application;

import cart.dao.MemberRewardPointDao;
import cart.dao.PurchaseOrderDao;
import cart.dao.PurchaseOrderItemDao;
import cart.domain.Member;
import cart.dto.product.ProductResponse;
import cart.dto.purchaseorder.PurchaseOrderItemInfoResponse;
import cart.dto.purchaseorder.response.PurchaseOrderItemResponse;
import cart.dto.purchaseorder.response.PurchaseOrderPageResponse;
import cart.dto.purchaseorder.response.PurchaseOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cart.TestFeatures.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

@JdbcTest
class PurchaseOrderServiceTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    private PurchaseOrderDao purchaseOrderDao;
    private PurchaseOrderItemDao purchaseOrderItemDao;
    private MemberRewardPointDao memberRewardPointDao;
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        purchaseOrderItemDao = new PurchaseOrderItemDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
        purchaseOrderDao = new PurchaseOrderDao(namedParameterJdbcTemplate, dataSource, purchaseOrderItemDao);
        memberRewardPointDao = new MemberRewardPointDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
        purchaseOrderService = new PurchaseOrderService(purchaseOrderDao, purchaseOrderItemDao, memberRewardPointDao);
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

    @DisplayName("특정 주문에 대한 정보를 조회할 수 있다")
    @Test
    void getPurchaseOrderByOrderId() {
        // given
        List<PurchaseOrderItemResponse> purchaseOrderItemResponses = List.of(
                new PurchaseOrderItemResponse(주문1_상품1.getQuantity(), ProductResponse.of(주문1_상품1.getProduct())),
                new PurchaseOrderItemResponse(주문1_상품2.getQuantity(), ProductResponse.of(주문1_상품2.getProduct()))
        );

        Long orderId = 1L;

        // when
        PurchaseOrderResponse purchaseOrderResponse = purchaseOrderService.getPurchaseOrderByOrderId(orderId);

        // then
        assertThat(purchaseOrderResponse).isEqualTo(
                new PurchaseOrderResponse(1L, LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                        10_000, 1_000, 500, purchaseOrderItemResponses)
        );
    }
}
