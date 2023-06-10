package cart.fixtures;

import static cart.fixtures.ProductFixtures.맥북_1500000원_ID_5;
import static cart.fixtures.ProductFixtures.바닐라_크림_콜드브루_5800원_ID_4;
import static cart.fixtures.ProductFixtures.아메리카노_4500원_ID_3;
import static cart.fixtures.ProductFixtures.유자_민트_티_5900원_ID_1;
import static cart.fixtures.ProductFixtures.자몽_허니_블랙티_5700원_ID_2;

import cart.domain.cart.CartItem;
import cart.domain.Member;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemFixtures {

    public static CartItem 유자_민트_티_ID_1_5개_29500원(Member member) {
        return new CartItem(1L, 5, 유자_민트_티_5900원_ID_1, member);
    }

    public static CartItem 자몽_허니_블랙티_ID_2_7개_39900원(Member member) {
        return new CartItem(2L, 7, 자몽_허니_블랙티_5700원_ID_2, member);
    }

    public static CartItem 아메리카노_ID_3_8개_36000원(Member member) {
        return new CartItem(3L, 8, 아메리카노_4500원_ID_3, member);
    }

    public static CartItem 바닐라_크림_콜드브루_ID_4_3개_17400원(Member member) {
        return new CartItem(4L, 3, 바닐라_크림_콜드브루_5800원_ID_4, member);
    }

    public static CartItem 맥북_ID_5_1개_1500000원(Member member) {
        return new CartItem(5L, 1, 맥북_1500000원_ID_5, member);
    }
}
