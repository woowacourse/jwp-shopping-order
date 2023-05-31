package cart.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class OrderIntegrationTest extends IntegrationTest{

    @Test
    void 고객은_자신의_선택한_장바구니에_있는_상품을_구매하면_장바구니에서_목록이_삭제된다(){

    }

    @Test
    void 고객이_상품을_구매할_때_포인트를_사용하면_기존_포인트가_차감된다(){

    }

    @Test
    void 고객이_상품을_구매하면_순수_구매_금액의_2_5퍼센트가_포인트로_적립된다(){

    }

    @Test
    void 고객이_상품을_구매할_때_자신이_가지고_있는_포인트보다_많은_포인트를_사용을_요청하면_실패한다(){

    }
}
