package anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Sql(value = {"/truncate-test.sql", "/data-test.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@JdbcTest(includeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public @interface RepositoryTest {
    /** TODO : truncate-test.sql 을 사용하지 않고서 해결할 방법?
     * 1. id 를 이용해서 조회하면 안됨
     * 2. id 를 이용해서 하게되더라도, id 를 얻어내기 위한 행위가 꼭 필요함, 그냥 쌩으로 갑자기 ID 를 바꾸는 것은 안됨
     */
}
