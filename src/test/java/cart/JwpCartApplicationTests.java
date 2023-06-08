package cart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(value = "classpath:truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class JwpCartApplicationTests {

    @Test
    void contextLoads() {
    }

}
