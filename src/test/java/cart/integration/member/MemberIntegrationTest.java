package cart.integration.member;

import cart.dao.member.MemberDao;
import cart.domain.member.Member;
import cart.domain.member.Rank;
import cart.dto.member.MemberResponse;
import cart.entity.MemberEntity;
import cart.integration.IntegrationTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static cart.fixture.MemberFixture.ako;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        Long id = memberDao.addMember(ako);
        MemberEntity updateMemberEntity = new MemberEntity(id, ako.getEmail(), ako.getPassword(), ako.getGrade(), ako.getTotalPurchaseAmount());
        memberDao.updateMember(updateMemberEntity);
        member = makeMember(updateMemberEntity);
    }

    private Member makeMember(final MemberEntity memberEntity) {
        return new Member(memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                Rank.valueOf(memberEntity.getGrade()),
                memberEntity.getTotalPurchaseAmount());
    }

    @Test
    @DisplayName("멤버를 조회한다.")
    void find_member() {
        // given
        MemberResponse expect = new MemberResponse(member);

        // when
        ExtractableResponse<Response> result = given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/member")
                .then().log().all()
                .extract();

        // then
        MemberResponse respone = result.jsonPath().getObject(".", MemberResponse.class);
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(respone).usingRecursiveComparison().isEqualTo(expect);
    }

}
