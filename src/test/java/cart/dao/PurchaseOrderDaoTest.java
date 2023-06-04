package cart.dao;

import cart.domain.Member;
import cart.domain.Pagination;
import cart.domain.purchaseorder.OrderStatus;
import cart.domain.purchaseorder.PurchaseOrderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cart.TestFeatures.*;
import static org.assertj.core.api.Assertions.*;

@JdbcTest
class PurchaseOrderDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;
    private PurchaseOrderDao purchaseOrderDao;
    private PurchaseOrderItemDao purchaseOrderItemDao;

    @BeforeEach
    void setup() {
        purchaseOrderItemDao = new PurchaseOrderItemDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
        purchaseOrderDao = new PurchaseOrderDao(namedParameterJdbcTemplate, dataSource, purchaseOrderItemDao);
    }

    @DisplayName("PurchaseOrder를 통해 주문을 저장한다")
    @Test
    void save() {
        // given
        PurchaseOrderInfo purchaseOrderInfo = new PurchaseOrderInfo(
                회원1,
                LocalDateTime.now(),
                10_000,
                1_000,
                OrderStatus.PENDING
        );

        // when
        Long saveId = purchaseOrderDao.save(purchaseOrderInfo);

        // then
        PurchaseOrderInfo orderResult = getPurchaseOrderById(saveId);
        assertThat(orderResult).usingRecursiveComparison()
                               .ignoringFields("id")
                               .isEqualTo(purchaseOrderInfo);
    }

    @DisplayName("PurchaseOrder에서 회원1의 주문 목록을 조회할 수 있다")
    @Test
    void findAllByMemberId() {
        // given
        Long memberId = 1L;

        // when
        List<PurchaseOrderInfo> resultOrderInfos = purchaseOrderDao.findAllByMemberId(memberId);

        // then
        assertThat(resultOrderInfos).usingRecursiveFieldByFieldElementComparator()
                                        .contains(회원1_주문1, 회원1_주문2, 회원1_주문3);
    }

    @DisplayName("PurchaseOrder를 특정 회원의 주문 목록을 페이징 처리를 통해 조회할 수 있다")
    @Test
    void findMemberByIdWithPagination() {
        // given
        Long memberId = 1L;
        Pagination pagination = Pagination.create(3, 1);

        // when
        List<PurchaseOrderInfo> resultOrderInfos = purchaseOrderDao.findMemberByIdWithPagination(memberId, pagination);

        // then
        assertThat(resultOrderInfos).usingRecursiveFieldByFieldElementComparator()
                                    .contains(회원1_주문1, 회원1_주문2, 회원1_주문3);
    }

    @DisplayName("PurchaseOrder 아이디를 통해 특정 주문을 조회할 수 있다")
    @Test
    void findById() {
        // given
        Long purchaseOrderId = 1L;

        // when
        PurchaseOrderInfo result = purchaseOrderDao.findById(purchaseOrderId);

        // then
        assertThat(result).usingRecursiveComparison()
                          .isEqualTo(회원1_주문1);
    }

    @DisplayName("특정 회원의 전체 주문 목록 개수를 조회할 수 있다")
    @Test
    void getTotalByMemberId() {
        // given
        Long memberId = 1L;

        // when
        int totalItemCount = purchaseOrderDao.getTotalByMemberId(memberId);

        // then
        assertThat(totalItemCount).isEqualTo(3);
    }

    @DisplayName("PurchaseOrder를 통해 특정 주문의 상태를 업데이트할 수 있다")
    @Test
    void updateStatus() {
        // given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        PurchaseOrderInfo updatePurchaseOrder =
                new PurchaseOrderInfo(1L, 회원1, LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                        10_000, 1_000, OrderStatus.PENDING);
        updatePurchaseOrder.changeStatus(OrderStatus.CANCELLED);

        // when
        purchaseOrderDao.updateStatus(updatePurchaseOrder);

        // then
        PurchaseOrderInfo updateResult = getPurchaseOrderById(updatePurchaseOrder.getId());
        assertThat(updateResult).usingRecursiveComparison()
                                .isEqualTo(updateResult);
    }

    private PurchaseOrderInfo getPurchaseOrderById(Long saveId) {
        PurchaseOrderInfo orderResult = namedParameterJdbcTemplate.queryForObject(
                "SELECT o.id as order_id, member.id as member_id, member.email, member.password, o.order_at, o.payment, o.used_point, o.status "
                        + "FROM purchase_order AS o "
                        + "JOIN member AS member ON o.member_id = member.id "
                        + "WHERE o.id = :id",
                new MapSqlParameterSource("id", saveId),
                getPurchaseOrderRowMapper()
        );
        return orderResult;
    }

    private RowMapper<PurchaseOrderInfo> getPurchaseOrderRowMapper() {
        return (rs, rowNum) ->
                new PurchaseOrderInfo(
                        rs.getLong("order_id"),
                        new Member(rs.getLong("member_id"), rs.getString("email"), rs.getString("password")),
                        rs.getTimestamp("order_at")
                          .toLocalDateTime(),
                        rs.getInt("payment"),
                        rs.getInt("used_point"),
                        OrderStatus.convertTo(rs.getString("status"))
                );
    }
}
