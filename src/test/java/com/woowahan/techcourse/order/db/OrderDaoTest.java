package com.woowahan.techcourse.order.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.order.domain.Order;
import com.woowahan.techcourse.order.domain.OrderFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class OrderDaoTest {

    private final OrderDao orderDao;

    @Autowired
    public OrderDaoTest(JdbcTemplate jdbcTemplate) {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 저장한_이후에_id_를_잘_불러온다() {
        // given
        // when
        Long result = orderDao.insert(OrderFixture.firstOrder);

        // then
        assertThat(result).isPositive();
    }

    @Nested
    class 저장한_이후에 {

        private Long orderId;

        @BeforeEach
        void setUp() {
            orderId = orderDao.insert(OrderFixture.firstOrder);
        }

        @Test
        void 저장한_데이터를_잘_불러온다() {
            // given
            // when
            Optional<Order> result = orderDao.findById(orderId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).isPresent();
                softly.assertThat(result.get().getMemberId()).isEqualTo(1L);
            });
        }

        @Test
        void 멤버_id_를_통해_조회할_수_있다() {
            // given
            // when
            List<Order> result = orderDao.findAllByMemberId(1L);

            // then
            assertThat(result).hasSize(1);
        }

        @Test
        void 없는_멤버일_경우_빈_리스트가_나온다() {
            // given
            // when
            List<Order> result = orderDao.findAllByMemberId(2L);

            // then
            assertThat(result).isEmpty();
        }
    }
}
