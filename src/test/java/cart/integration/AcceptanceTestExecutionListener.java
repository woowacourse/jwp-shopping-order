package cart.integration;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.List;

public class AcceptanceTestExecutionListener implements TestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        final JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        final List<String> truncateQueries = getTruncateQueries(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateQueries);
    }

    private List<String> getTruncateQueries(final JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList("SELECT CONCAT('TRUNCATE TABLE ', TABLE_NAME, ' RESTART IDENTITY;') AS q " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_SCHEMA = 'PUBLIC'",
                String.class);
    }

    private JdbcTemplate getJdbcTemplate(final TestContext testContext) {
        return testContext.getApplicationContext().getBean(JdbcTemplate.class);
    }

    private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateQueries) {
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE");
        for (String truncateQuery : truncateQueries) {
            execute(jdbcTemplate, truncateQuery);
        }
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE");
    }

    private void execute(final JdbcTemplate jdbcTemplate, final String query) {
        jdbcTemplate.execute(query);
    }
}
