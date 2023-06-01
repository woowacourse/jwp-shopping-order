package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DefaultPointManagerTest {

    private DefaultPointManager defaultPointManager;

    @BeforeEach
    void setUp() {
        defaultPointManager = new DefaultPointManager();
    }

    @Test
    void getPoint() {
        final Price price = Price.valueOf(1000);
        final Point result = defaultPointManager.getPoint(price);
        assertThat(result).isEqualTo(Point.valueOf(50));
    }

    @Test
    void getPointRoundDown() {
        final Price price = Price.valueOf(999);
        final Point result = defaultPointManager.getPoint(price);
        assertThat(result).isEqualTo(Point.valueOf(49));
    }

    @CsvSource({"2023-05-31 10:00:00,2023-11-30 10:00:00", "2023-06-01 10:00:00,2023-11-31 10:00:00"})
    @ParameterizedTest
    void getExpiredAt(final String created, final String expired) {
        final Timestamp createdAt = Timestamp.valueOf(created);
        final Timestamp result = defaultPointManager.getExpiredAt(createdAt);
        assertThat(result).isEqualTo(Timestamp.valueOf(expired));
    }
}
