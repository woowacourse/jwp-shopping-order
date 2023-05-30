package cart.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import cart.TestSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PaginatorTest {

    private final Order orderAt1999Payed30000PointUsed1000 = new Order(1L, LocalDateTime.of(1999, 1, 1, 1, 1), 30000,
        1000, 1500, OrderStatus.PENDING, List.of(TestSource.quantityAndProduct1));
    private final Order orderAt2000Payed20000PointUsed900 = new Order(1L, LocalDateTime.of(2000, 1, 1, 1, 1), 20000,
        900, 1000, OrderStatus.PENDING, List.of(TestSource.quantityAndProduct2));
    private final Order orderAt2001Payed10000PointUsed800 = new Order(1L, LocalDateTime.of(2001, 1, 1, 1, 1), 10000,
        800, 500, OrderStatus.PENDING, List.of(TestSource.quantityAndProduct3));

    private final List<Order> orders = List.of(orderAt1999Payed30000PointUsed1000, orderAt2000Payed20000PointUsed900,
        orderAt2001Payed10000PointUsed800);

    @Nested
    class 정렬_기능_테스트 {

        @Test
        void 날짜에_내림차순으로_주문을_정렬한다() {
            // given
            Paginator<Order> paginator = new Paginator<>(10, Order::getOrderAt);

            // when
            List<Order> paginatedOrders = paginator.paginate(orders, 1).getContents();

            // then
            assertThat(paginatedOrders).containsExactly(
                orderAt2001Payed10000PointUsed800,
                orderAt2000Payed20000PointUsed900,
                orderAt1999Payed30000PointUsed1000
            );
        }

        @Test
        void 지불_금액에_내림차순으로_주문을_정렬한다() {
            // given
            Paginator<Order> paginator = new Paginator<>(10, Order::getPayAmount);

            // when
            List<Order> paginatedOrders = paginator.paginate(orders, 1).getContents();

            // then
            assertThat(paginatedOrders).containsExactly(
                orderAt1999Payed30000PointUsed1000,
                orderAt2000Payed20000PointUsed900,
                orderAt2001Payed10000PointUsed800
            );
        }
    }

    @Nested
    class 페이지_분할_기능_테스트 {

        @Test
        void 페이지당_지정된_개수의_주문_이력만을_반환한다() {
            // given
            int dataPerPage = 1;
            Paginator<Order> paginator = new Paginator<>(dataPerPage, Order::getOrderAt);

            // when
            Page<Order> firstPage = paginator.paginate(orders, 1);
            Page<Order> secondPage = paginator.paginate(orders, 2);
            Page<Order> thirdPage = paginator.paginate(orders, 3);

            // then
            assertAll(() -> {
                assertThat(firstPage.getCurrentPage()).isEqualTo(1);
                assertThat(secondPage.getCurrentPage()).isEqualTo(2);
                assertThat(thirdPage.getCurrentPage()).isEqualTo(3);

                assertThat(firstPage.getContents()).containsExactly(orderAt2001Payed10000PointUsed800);
                assertThat(secondPage.getContents()).containsExactly(orderAt2000Payed20000PointUsed900);
                assertThat(thirdPage.getContents()).containsExactly(orderAt1999Payed30000PointUsed1000);
            });
        }
    }

    @Test
    void 전체_페이지_계산_기능_테스트() {
        // given
        int dataPerPage = 2;
        Paginator<Order> paginator = new Paginator<>(dataPerPage, Order::getOrderAt);

        // when
        Page<Order> secondPage = paginator.paginate(orders, 2);

        // then
        assertAll(() -> {
            assertThat(secondPage.getContents()).containsExactly(orderAt1999Payed30000PointUsed1000);
            assertThat(secondPage.getPageSize()).isEqualTo(2);
            assertThat(secondPage.getCurrentPage()).isEqualTo(2);
            assertThat(secondPage.getTotalPages()).isEqualTo(2);
        });
    }
}
