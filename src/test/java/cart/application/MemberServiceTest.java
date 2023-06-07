package cart.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import cart.dto.MemberResponse;

@SpringBootTest
@Sql("/data.sql")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("등록된 모든 member의 정보를 반환한다.")
    void findAll() {
        // given
        final List<Long> ids = List.of(1L, 2L, 3L);
        final List<String> emails = List.of("a@a.com", "b@b.com", "c@c.com");
        final List<String> passwords = List.of("1234", "1234", "1234");
        final List<String> grades = List.of("gold", "silver", "bronze");

        // when
        final List<MemberResponse> results = memberService.findAll();
        final List<Long> idResults = results.stream()
                .map(MemberResponse::getId)
                .collect(Collectors.toList());

        final List<String> emailResults = results.stream()
                .map(MemberResponse::getEmail)
                .collect(Collectors.toList());

        final List<String> passwordResults = results.stream()
                .map(MemberResponse::getPassword)
                .collect(Collectors.toList());

        final List<String> gradeResults = results.stream()
                .map(MemberResponse::getGrade)
                .collect(Collectors.toList());

        //then
        Assertions.assertAll(
                () -> assertThat(results.size()).isEqualTo(3),
                () -> assertThat(idResults).containsExactlyInAnyOrderElementsOf(ids),
                () -> assertThat(emailResults).containsExactlyInAnyOrderElementsOf(emails),
                () -> assertThat(passwordResults).containsExactlyInAnyOrderElementsOf(passwords),
                () -> assertThat(gradeResults).containsExactlyInAnyOrderElementsOf(grades)
        );
    }
}
