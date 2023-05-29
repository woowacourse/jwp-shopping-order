package cart.acceptance;

import cart.dao.MemberDao;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cart.fixture.MemberFixtures.MEMBER_GITCHAN;
import static cart.fixture.ProductFixtures.PRODUCT_REQUEST_CAMERA_EOS_M200;
import static cart.steps.CartItemSteps.*;
import static cart.steps.ProductSteps.제품_추가하고_아이디_반환;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

public class CartItemAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void 카트에_아이템을_추가한다() {
        memberDao.addMember(MEMBER_GITCHAN);
        final Long productId = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);

        final ExtractableResponse<Response> response = 카트에_아이템_추가_요청(MEMBER_GITCHAN, new CartItemRequest(productId));
        final Long cartItemId = 추가된_카트_아이템_아이디_반환(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/cart-items/" + cartItemId)
        );
    }

    @Test
    void 카트에_저장된_아이템의_개수를_변경한다() {
        memberDao.addMember(MEMBER_GITCHAN);
        final Long productId = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);
        final Long cartItemId = 카트에_아이템_추가하고_아이디_반환(MEMBER_GITCHAN, new CartItemRequest(productId));

        final ExtractableResponse<Response> response = 카트에_저장된_아이템의_개수_변경_요청(
                MEMBER_GITCHAN,
                cartItemId,
                new CartItemQuantityUpdateRequest(5));

        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 카트에_저장된_아이템을_삭제한다() {
        memberDao.addMember(MEMBER_GITCHAN);
        final Long productId = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);
        final Long cartItemId = 카트에_아이템_추가하고_아이디_반환(MEMBER_GITCHAN, new CartItemRequest(productId));

        final ExtractableResponse<Response> response = 카트에_저장된_아이템_삭제_요청(MEMBER_GITCHAN, cartItemId);

        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }
}
