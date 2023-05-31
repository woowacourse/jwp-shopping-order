package cart.dao;

import cart.entity.OrderItemEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Repository
public class OrderItemDao {
    
    private static final int AFFECTED_ROW_COUNT_WHEN_INSERT = 1; 

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderItemDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(final List<OrderItemEntity> sources) {
        final SqlParameterSource[] allParams = sources.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        try {
            int[] affectedRows = insertAction.executeBatch(allParams);
            validateAffectedRowsWhenInsertAll(affectedRows);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("잘못된 order id 혹은 product id 입니다.");
        }
    }

    private void validateAffectedRowsWhenInsertAll(final int[] affectedRows) {
        final boolean isNormal = Arrays.stream(affectedRows)
                .allMatch(value -> value == AFFECTED_ROW_COUNT_WHEN_INSERT);
        if (isNormal) {
            return;
        }
        throw new RuntimeException("주문 상품 목록 등록 시 다른 데이터에도 영향이 갔습니다.");
    }
}
