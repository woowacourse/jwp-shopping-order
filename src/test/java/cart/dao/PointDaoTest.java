package cart.dao;

import cart.entity.PointEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql({"classpath:test_init.sql"})
@ActiveProfiles("test")
class PointDaoTest {

    private JdbcTemplate jdbcTemplate;
    private PointDao pointDao;

    @Autowired
    public PointDaoTest(JdbcTemplate jdbcTemplate, PointDao pointDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.pointDao = pointDao;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into product (name, price, image_url) values ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80')");

        jdbcTemplate.update("insert into member(email, password) values('konghana@com', '1234')");

        jdbcTemplate.update("insert into orders(member_id, orders_status_id) values(1, 1)");
        jdbcTemplate.update("insert into orders(member_id, orders_status_id) values(1, 1)");

        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 1, 3, 30000)");
        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(1, 2, 2, 40000)");
        jdbcTemplate.update("insert into orders_item(orders_id, product_id, quantity, total_price) values(2, 3, 2, 26000)");

        jdbcTemplate.update("insert into point(member_id, orders_id, earned_point, comment, create_at, expired_at) values(1, 1, 5600, '주문 포인트 적립', '2023-06-02', '2023-09-30')");
        jdbcTemplate.update("insert into point(member_id, orders_id, earned_point, comment, create_at, expired_at) values(1, 2, 1300, '주문 포인트 적립', '2023-06-15', '2023-09-30')");
    }

    @DisplayName("주문 번호를 기준으로 포인트를 조회할 수 있다.")
    @Test
    void findByOrderId() {
        PointEntity expected = new PointEntity(1L, 5600, "주문 포인트 적립", LocalDate.of(2023, 06, 02), LocalDate.of(2023, 9, 30));

        PointEntity point = pointDao.findExistsBy(1L, 1L);

        assertThat(point).isEqualTo(expected);
    }

    @DisplayName("주문 번호를 기준으로 포인트를 조회할 수 있다.")
    @Test
    void findByMemberId() {
        PointEntity expected1 = new PointEntity(1L, 5600, "주문 포인트 적립", LocalDate.of(2023, 06, 02), LocalDate.of(2023, 9, 30));
        PointEntity expected2 = new PointEntity(2L, 1300, "주문 포인트 적립", LocalDate.of(2023, 06, 15), LocalDate.of(2023, 9, 30));

        List<PointEntity> points = pointDao.findByMemberId(1L);

        assertThat(points).containsExactlyInAnyOrder(expected1, expected2);
    }

    @DisplayName("주문 번호와 멤버 아이디를 기준으로 포인트를 저장할 수 있다.")
    @Test
    void save() {
        PointEntity pointEntity = new PointEntity(3L, 1000, "테스트 주문 포인트", LocalDate.of(2023, 06, 02), LocalDate.of(2023, 9, 30));

        pointDao.save(1L, 2L, pointEntity);

        Integer point = jdbcTemplate.queryForObject("select earned_point from point where orders_id = 2 and comment = '테스트 주문 포인트'", Integer.class);

        assertThat(point).isEqualTo(1000);
    }

    @DisplayName("주문 번호를 기준으로 포인트를 삭제할 수 있다.")
    @Test
    void deleteByOrderId() {
        pointDao.delete(1L, 1L);

        assertThatThrownBy(() -> jdbcTemplate.queryForObject("select id from point where orders_id = 1 and status = 1 ", Long.class))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
