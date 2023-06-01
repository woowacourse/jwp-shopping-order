package cart.service.member;

import cart.dto.member.MemberCreateRequest;
import cart.dto.member.MemberResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @DisplayName("멤버를 저장한다.")
    @Test
    void save_member() {
        // given
        MemberCreateRequest req = new MemberCreateRequest("c@c.com", "1234");

        // when
        memberService.createMember(req);

        // then
        List<MemberResponse> result = memberService.findAll();
        assertThat(result.size()).isEqualTo(3);
    }

    @DisplayName("멤버를 전체 조회한다.")
    @Test
    void find_all_members() {
        // given
        MemberCreateRequest req = new MemberCreateRequest("c@c.com", "1234");
        memberService.createMember(req);

        // when
        List<MemberResponse> result = memberService.findAll();

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.get(2).getEmail()).isEqualTo(req.getEmail())
        );
    }

    @DisplayName("멤버를 단건 조회한다.")
    @Test
    void find_member() {
        // given
        MemberCreateRequest req = new MemberCreateRequest("c@c.com", "1234");
        memberService.createMember(req);

        // when
        MemberResponse result = memberService.findById(3L);

        // then
        assertAll(
                () -> assertThat(result.getEmail()).isEqualTo(req.getEmail()),
                () -> assertThat(result.getPassword()).isEqualTo(req.getPassword())
        );
    }

    @DisplayName("멤버를 삭제한다.")
    @Test
    void delete_member() {
        // given
        MemberCreateRequest req = new MemberCreateRequest("c@c.com", "1234");
        memberService.createMember(req);

        // when
        memberService.deleteById(3L);

        // then
        List<MemberResponse> result = memberService.findAll();
        assertThat(result.size()).isEqualTo(2);
    }

}
