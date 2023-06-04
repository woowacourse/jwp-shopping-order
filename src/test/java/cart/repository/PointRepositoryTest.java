package cart.repository;

import cart.dao.PointDao;
import cart.dao.PointHistoryDao;
import cart.domain.*;
import cart.exception.OrderException;
import cart.exception.OrderServerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static cart.ProductFixture.product1;
import static cart.ProductFixture.product2;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"classpath:test_init.sql"})
@ActiveProfiles("test")
class PointRepositoryTest {

    private PointRepository pointRepository;
    private PointDao pointDao;
    private PointHistoryDao pointHistoryDao;
    private JdbcTemplate jdbcTemplate;

    private Member member;

    @Autowired
    public PointRepositoryTest(PointRepository pointRepository, PointDao pointDao, PointHistoryDao pointHistoryDao, JdbcTemplate jdbcTemplate) {
        this.pointRepository = pointRepository;
        this.pointDao = pointDao;
        this.pointHistoryDao = pointHistoryDao;
        this.jdbcTemplate = jdbcTemplate;
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

        jdbcTemplate.update("insert into point(member_id, orders_id, earned_point, comment, create_at, expired_at) values(1, 1, 5600, '주문 포인트 적립', '2023-07-02', '2023-10-31')");
        jdbcTemplate.update("insert into point(member_id, orders_id, earned_point, comment, create_at, expired_at) values(1, 2, 1300, '주문 포인트 적립', '2023-06-15', '2023-09-30')");
        jdbcTemplate.update("insert into point(member_id, orders_id, earned_point, comment, create_at, expired_at) values(1, 3, 400, '주문 포인트 적립', '2023-02-15', '2023-05-31')");

        jdbcTemplate.update("insert into point_history(orders_id, point_id, used_point) values(2, 1, 1000)");
        jdbcTemplate.update("insert into point_history(orders_id, point_id, used_point) values(3, 1, 2000)");

        member = new Member(1L, "kong@com", "1234");
    }

    @DisplayName("한 유저에 대한 사용가능한 포인트 정보를 구할 수 있다. 이때 정렬된 값이 반환된다.")
    @Test
    void findUsablePointsByMemberId() {
        Points points = pointRepository.findUsablePointsByMemberId(1L);

        Point point1 = Point.of(1L, 2600, "주문 포인트 적립", LocalDate.of(2023, 7, 2), LocalDate.of(2023, 10, 31));
        Point point2 = Point.of(2L, 1300, "주문 포인트 적립", LocalDate.of(2023, 6, 15), LocalDate.of(2023, 9, 30));

        assertThat(points.getPoints()).containsExactly(point2, point1);
    }

    @DisplayName("한 유저의 한 주문에 대한 포인트 정보를 구할 수 있다.")
    @Test
    void findBy_success() {
        Point point = pointRepository.findBy(1L, 1L);

        Point expected = Point.of(1L, 5600, "주문 포인트 적립", LocalDate.of(2023, 7, 2), LocalDate.of(2023, 10, 31));

        assertThat(point).isEqualTo(expected);
    }

    @DisplayName("데이터베이스에 저장되지 않은 포인트를 요청할 때 예외가 발생한다..")
    @ParameterizedTest
    @CsvSource(value = {"1:100", "100:1"}, delimiter = ':')
    void findBy_fail(Long memberId, Long orderId) {
        assertThatThrownBy(() -> pointRepository.findBy(memberId, orderId))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("입력한 포인트가 없습니다.");
    }

    @DisplayName("한 주문에 대한 포인트를 저장할 수 있다.")
    @Test
    void save_success() {
        pointRepository.save(member.getId(), 3L, Point.of(3000, "테스트 주문 포인트", LocalDate.of(2099, 03, 03)));

        Integer point = jdbcTemplate.queryForObject("select earned_point from point where orders_id = 3 and comment = '테스트 주문 포인트'", Integer.class);

        assertThat(point).isEqualTo(3000);
    }

    @DisplayName("이미 저장되지 않은 주문이나 멤버에 대한 적립을 할 경우에는 예외가 발생한다..")
    @ParameterizedTest
    @CsvSource(value = {"1:100", "100:1"}, delimiter = ':')
    void save_fail(Long memberId, Long orderId) {
        assertThatThrownBy(() -> pointRepository.save(memberId, orderId, Point.of(3000, "테스트 주문 포인트", LocalDate.of(2099, 03, 03))))
                .isInstanceOf(OrderServerException.class)
                .hasMessageContaining("데이터베이스에 포인트를 적립할 수 없습니다.");
    }

    @DisplayName("기존에 적립한 포인트를 사용하지 않았다면 포인트를 삭제할 수 있다.")
    @Test
    void delete_success() {
        assertThatCode(() -> pointRepository.delete(1L, 2L))
                .doesNotThrowAnyException();
    }

    @DisplayName("기존에 적립한 포인트를 사용했다면 포인트를 삭제할 때 예외가 발생한다.")
    @Test
    void delete_fail_1() {
        assertThatThrownBy(() -> pointRepository.delete(1L, 1L))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("사용된 포인트를 취소할 수 없습니다.");
    }

    @DisplayName("적립되지 않은 포인트에 대해 삭제할 때 예외가 발생한다.")
    @Test
    void delete_fail_2() {
        assertThatThrownBy(() -> pointRepository.delete(1L, 100L))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("입력한 포인트가 없습니다.");
    }
}
