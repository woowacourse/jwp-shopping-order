package cart.fixtures;


import cart.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixtures {

    public static final Product 유자_민트_티_ID_1 = new Product(
            1L,
            "유자 민트 티",
            5_900,
            "https://image.istarbucks.co.kr/upload/store/skuimg/2022/04/[9200000002959]_20220411155904911.jpg");

    public static final Product 자몽_허니_블랙티_ID_2 = new Product(
            2L,
            "자몽 허니 블랙티",
            5_700,
            "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000000187]_20210419131229682.jpg");

    public static final Product 아메리카노_ID_3 = new Product(
            3L,
            "아메리카노",
            4_500,
            "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937808.jpg");

    public static final Product 바닐라_크림_콜드브루_ID_4 = new Product(
            4L,
            "바닐라 크림 콜드브루",
            5_800,
            "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000000487]_20210430112319040.jpg");
}
