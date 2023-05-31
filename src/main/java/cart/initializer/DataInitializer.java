package cart.initializer;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    public DataInitializer(final ProductRepository productRepository,
                           final MemberRepository memberRepository,
                           final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @PostConstruct
    public void init() {
        final Long 깃짱 = productRepository.createProduct(new Product("깃짱", 13000, "https://lh3.googleusercontent.com/u/0/drive-viewer/AFGJ81oHOa1sliugOaQaRcinBq9LyWbwL8X1_jFn_WhgE6K9RaHyeb7UUHQaW8APSDVuF3oafXcf-TEGpTNLks_BK2nyI6EtRQ=w1470-h1546"));
        final Long 제리 = productRepository.createProduct(new Product("제리", 13000, "https://lh3.googleusercontent.com/u/0/drive-viewer/AFGJ81qOHC6fmRBVgeBwdwqxQUV4nrhF4U-kfihqaGbtrYwrUTXvaxCC0bRnOoQiAjhPGpztqnyNqX3ZmWI_CFMszdLwMGwiOg=w1470-h1546"));
        final Long 호이 = productRepository.createProduct(new Product("호이", 13000, "https://lh3.googleusercontent.com/u/0/drive-viewer/AFGJ81oHOa1sliugOaQaRcinBq9LyWbwL8X1_jFn_WhgE6K9RaHyeb7UUHQaW8APSDVuF3oafXcf-TEGpTNLks_BK2nyI6EtRQ=w1470-h1546"));
        final Long 링링 = productRepository.createProduct(new Product("링링", 13000, "https://lh3.googleusercontent.com/u/0/drive-viewer/AFGJ81oB2HgBpMMwH10OZ_IEK9VocgjXLZWZScpK3ckZCao1cQQ4mvf7r-_tRWoUDvTULut4MJezdsqrhRX_YIwOrWthAFNi=w1470-h1546"));
        final Long 글로 = productRepository.createProduct(new Product("글로", 13000, "https://lh3.googleusercontent.com/u/0/drive-viewer/AFGJ81qK0iC7W3jm65K99d1hFNxvLjJfoqR1HuQusLcj2wGfl4J3vn-NTBMTlRhONMozARu-0zZws6OJmVT0EQGxKOjJ-ELitA=w1470-h1546"));
        final Long 주노 = productRepository.createProduct(new Product("주노", 130000, "https://lh3.googleusercontent.com/u/0/drive-viewer/AFGJ81qqjh0GfuPMQ9cdRtm87_mDsspllcIMj7-xGLal4AP1EBaGPTXTjBb1sgxk-_Vg8Z93PLDGHWNiXmKf62JKdTKW5M6Sfg=w1470-h1546"));
        final Long 리내 = productRepository.createProduct(new Product("이리내", 13000, "https://lh3.googleusercontent.com/drive-viewer/AFGJ81q1kSc2EAZwZT_zwpumBoAFR3VqLcnkWHBVY5gNUifsVnhclYyjQM_4I0i3Hhi4a3wq7Tvn1kDNjV_205z3_tH96avW=w1470-h1546"));

        final Member savedMember1 = memberRepository.addMember(new Member("a@a.com", "1234"));
        final Member savedMember2 = memberRepository.addMember(new Member("b@b.com", "1234"));
        final Member savedMember3 = memberRepository.addMember(new Member("ringlo@email.com", "ringlo1010235"));

        cartItemRepository.save(new CartItem(savedMember1, productRepository.getProductById(깃짱)));
        cartItemRepository.save(new CartItem(savedMember1, productRepository.getProductById(제리)));
        cartItemRepository.save(new CartItem(savedMember1, productRepository.getProductById(호이)));

        cartItemRepository.save(new CartItem(savedMember2, productRepository.getProductById(깃짱)));
        cartItemRepository.save(new CartItem(savedMember2, productRepository.getProductById(제리)));

        cartItemRepository.save(new CartItem(savedMember3, productRepository.getProductById(깃짱)));
    }
}
