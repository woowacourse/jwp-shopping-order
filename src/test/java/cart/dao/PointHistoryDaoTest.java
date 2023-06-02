package cart.dao;

import cart.entity.PointHistoryEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"classpath:test_init.sql"})
@ActiveProfiles("test")
class PointHistoryDaoTest {

    private JdbcTemplate jdbcTemplate;
    private PointHistoryDao pointHistoryDao;

    @Autowired
    public PointHistoryDaoTest(JdbcTemplate jdbcTemplate, PointHistoryDao pointHistoryDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.pointHistoryDao = pointHistoryDao;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into product (name, price, image_url) values ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80')");

        jdbcTemplate.update("insert into member(email, password) values('konghana@com', '1234')");

        jdbcTemplate.update("insert into orders(member_id, orders_status_id) values(1, 1)");
        jdbcTemplate.update("insert into orders(member_id, orders_status_id) values(1, 1)");
        jdbcTemplate.update("insert into orders(member_id, orders_status_id) values(1, 1)");

        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 1, 3, 30000)");
        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 2, 2, 40000)");
        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(2, 3, 2, 26000)");
        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 1, 3, 30000)");

        jdbcTemplate.update("insert into point(member_id, orders_id, earned_point, comment, create_at, expired_at) values(1, 1, 5600, '주문 포인트 적립', '2023-06-02', '2023-09-30')");
        jdbcTemplate.update("insert into point(member_id, orders_id, earned_point, comment, create_at, expired_at) values(1, 2, 1250, '주문 포인트 적립', '2023-06-15', '2023-09-30')");
        jdbcTemplate.update("insert into point(member_id, orders_id, earned_point, comment, create_at, expired_at) values(1, 3, 1400, '주문 포인트 적립', '2023-06-15', '2023-09-30')");

        jdbcTemplate.update("insert into point_history(orders_id, point_id, used_point) values(2, 1, 1000)");
        jdbcTemplate.update("insert into point_history(orders_id, point_id, used_point) values(2, 1, 2000)");
    }

    @DisplayName("")
    @Test
    void findByPointIds() {
        PointHistoryEntity expected1 = new PointHistoryEntity(1L, 2L, 1L, 1000);
        PointHistoryEntity expected2 = new PointHistoryEntity(2L, 3L, 1L, 2000);

        List<PointHistoryEntity> pointHistoryEntities = pointHistoryDao.findByPointIds(List.of(1L));

        assertThat(pointHistoryEntities).containsAnyOf(expected1, expected2);
    }
}
