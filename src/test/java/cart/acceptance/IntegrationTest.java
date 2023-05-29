package cart.acceptance;

import cart.dao.MemberDao;
import cart.domain.Member;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(listeners = AcceptanceTestExecutionListener.class, mergeMode = MERGE_WITH_DEFAULTS)
@Sql(scripts = "classpath:data.sql", executionPhase = AFTER_TEST_METHOD)
public abstract class IntegrationTest {

    @Autowired
    protected MemberDao memberDao;

    protected Member member;

    @BeforeEach
    void init(@LocalServerPort int port) {
        RestAssured.port = port;
        member = new Member(1L, "a@a.com", "1234");
    }
}
