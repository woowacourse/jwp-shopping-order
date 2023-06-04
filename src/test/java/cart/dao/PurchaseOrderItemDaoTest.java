package cart.dao;

import cart.domain.Product;
import cart.domain.purchaseorder.PurchaseOrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;

import static cart.TestFeatures.*;
import static org.assertj.core.api.Assertions.*;

@JdbcTest
class PurchaseOrderItemDaoTest {

    private final RowMapper<PurchaseOrderItem> rowMapper = (rs, rowNum) ->
            new PurchaseOrderItem(
                    rs.getLong("order_id"),
                    new Product(rs.getLong("p_id"), rs.getString("name"),
                            rs.getInt("price"), rs.getString("image_url")),
                    rs.getInt("quantity")
            );

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;
    private PurchaseOrderItemDao purchaseOrderItemDao;

    @BeforeEach
    void setup() {
        purchaseOrderItemDao = new PurchaseOrderItemDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
    }

    @DisplayName("PurchaseOrderItem를 통해 주문 상품을 저장한다")
    @Test
    void save() {
        // given
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(상품1_치킨, 2);

        // when
        Long saveId = purchaseOrderItemDao.save(purchaseOrderItem, 2L);

        // then
        PurchaseOrderItem result = namedParameterJdbcTemplate.queryForObject(
                "SELECT oi.id as order_id, p.id as p_id, p.name as name, p.price as price, p.image_url as image_url, oi.quantity "
                        + "FROM purchase_order_item AS oi "
                        + "JOIN product AS p ON oi.product_id = p.id "
                        + "WHERE oi.id = :id",
                new MapSqlParameterSource("id", saveId),
                rowMapper
        );
        assertThat(result).usingRecursiveComparison()
                          .ignoringFields("id")
                          .isEqualTo(purchaseOrderItem);
    }

    @DisplayName("PurchaseOrderItem 목록을 한 번에 저장할 수 있다")
    @Test
    void saveAll() {
        // given
        Long saveId = 1L;
        int originalSize = findAllByPurchaseOrderId(saveId).size();
        List<PurchaseOrderItem> purchaseOrderItem =
                List.of(new PurchaseOrderItem(상품1_치킨, 2),
                        new PurchaseOrderItem(상품2_샐러드, 4));

        // when
        purchaseOrderItemDao.saveAll(purchaseOrderItem, saveId);

        // then
        int resultSize = findAllByPurchaseOrderId(saveId).size();
        assertThat(resultSize).isEqualTo(originalSize + purchaseOrderItem.size());
    }

    private List<PurchaseOrderItem> findAllByPurchaseOrderId(Long order_id) {
        String sql = "SELECT oi.id as order_id, p.id as p_id, p.name as name, p.price as price, p.image_url as image_url, oi.quantity "
                + "FROM purchase_order_item AS oi "
                + "JOIN product AS p ON oi.product_id = p.id "
                + "WHERE oi.order_id = :order_id";
        SqlParameterSource source = new MapSqlParameterSource("order_id", order_id);
        return namedParameterJdbcTemplate.query(sql, source, rowMapper);
    }

    @DisplayName("PurchaseOrder의 아이디를 통해 PurchasedOrderItem 목록을 조회한다")
    @Test
    void findAllByPurchaseOrderId() {
        // given
        Long purchaseOrderId = 1L;

        // when
        List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemDao.findAllByPurchaseOrderId(purchaseOrderId);

        // then
        assertThat(purchaseOrderItems).usingRecursiveFieldByFieldElementComparator()
                                      .contains(주문1_상품1, 주문1_상품2);
    }
}
