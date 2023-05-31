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
        final Long 깃짱 = productRepository.createProduct(new Product("깃짱", 13000, "https://lh3.google.com/u/0/d/1YelRXToHj3qh19EiTZzXHa5D8UVSy95n=w2940-h1546-iv1"));
        final Long 제리 = productRepository.createProduct(new Product("제리", 13000, "https://lh3.google.com/u/0/d/1lKBDu_66U0dX15yOtHieAuuIclKDEWB-=w1294-h1546-iv1"));
        final Long 호이 = productRepository.createProduct(new Product("호이", 13000, "https://lh3.google.com/u/0/d/1Pcyd8ngTr0TFBBSnANJsXhCwnnXGCLZz=w1294-h1546-iv1"));
        final Long 링링 = productRepository.createProduct(new Product("링링", 13000, "https://lh3.google.com/u/0/d/1bMoOTubWF_gKvF4rSxN3woBY6PjjQMl4=w1294-h1546-iv1"));
        final Long 글로 = productRepository.createProduct(new Product("글로", 13000, "https://lh3.google.com/u/0/d/1KIhTiIEn0NyXVYpY4Lv9XqzAAAyBIkVx=w1294-h1546-iv1"));
        final Long 주노 = productRepository.createProduct(new Product("주노", 130000, "https://lh3.google.com/u/0/d/1e-ssyT6u7aREJB7mNpR9jMY_wX3XYFBS=w1294-h1546-iv1"));
        final Long 리내 = productRepository.createProduct(new Product("이리내", 13000, "https://lh3.google.com/u/0/d/1H7YT80v_2Pif2QOyy4uqll-NEKD030Qq=w1294-h1546-iv1"));

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
