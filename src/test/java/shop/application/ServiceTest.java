package shop.application;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql("/initialization.sql")
@ActiveProfiles("test")
@SpringBootTest
public class ServiceTest {
}
