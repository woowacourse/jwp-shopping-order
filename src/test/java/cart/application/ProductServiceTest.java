package cart.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.exception.BadRequestException;

class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @Mock
    private CartItemDao cartItemDao;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productDao, cartItemDao);
    }

    @Test
    @DisplayName("모든 상품 조회")
    public void testFindAllProducts() {
        List<Product> products = Arrays.asList(
            new Product(1L, "Product 1", 1000, "http://www.example.com/product1.jpg"),
            new Product(2L, "Product 2", 2000, "http://www.example.com/product2.jpg"),
            new Product(3L, "Product 3", 3000, "http://www.example.com/product3.jpg")
        );

        when(productDao.findAll()).thenReturn(products);

        List<ProductResponse> response = productService.findAll();

        assertNotNull(response);
        assertEquals(products.size(), response.size());

        verify(productDao, times(1)).findAll();
    }

    @Test
    @DisplayName("상품 ID로 상품 조회 - 상품 존재")
    public void testFindProductById_ExistingProduct() {
        Long productId = 1L;
        Product product = new Product(productId, "Product 1", 1000, "http://www.example.com/product1.jpg");

        when(productDao.findById(productId)).thenReturn(product);

        ProductResponse response = productService.findById(productId);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getPrice(), response.getPrice());
        assertEquals(product.getImageUrl(), response.getImageUrl());

        verify(productDao, times(1)).findById(productId);
    }

    @Test
    @DisplayName("상품 ID로 상품 조회 - 상품 미존재")
    public void testFindProductById_NonExistingProduct() {
        Long productId = 1L;

        when(productDao.findById(productId)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> productService.findById(productId),
            "Expected BadRequestException to be thrown");

        verify(productDao, times(1)).findById(productId);
    }

    @Test
    @DisplayName("상품 추가")
    public void testAddProduct() {
        ProductRequest productRequest = new ProductRequest("Product 1", 1000, "http://www.example.com/product1.jpg");
        Product product = new Product("Product 1", 1000, "http://www.example.com/product1.jpg");
        Long productId = 1L;

        when(productDao.save(product)).thenReturn(productId);

        Long response = productService.add(productRequest);

        assertNotNull(response);
    }

    @Test
    @DisplayName("상품 수정 - 상품 존재")
    public void testUpdateProduct_ExistingProduct() {
        Long productId = 1L;
        ProductRequest productRequest = new ProductRequest("Updated Product", 2000,
            "http://www.example.com/updated.jpg");
        Product product = new Product(productId, "Product 1", 1000, "http://www.example.com/product1.jpg");

        when(productDao.findById(productId)).thenReturn(product);

        assertDoesNotThrow(() -> productService.update(productId, productRequest),
            "Expected no exception to be thrown");

        verify(productDao, times(1)).findById(productId);
    }

    @Test
    @DisplayName("상품 수정 - 상품 미존재")
    public void testUpdateProduct_NonExistingProduct() {
        Long productId = 1L;
        ProductRequest productRequest = new ProductRequest("Updated Product", 2000,
            "http://www.example.com/updated.jpg");

        when(productDao.findById(productId)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> productService.update(productId, productRequest),
            "Expected BadRequestException to be thrown");

        verify(productDao, times(1)).findById(productId);
        verify(productDao, never()).update(anyLong(), any(Product.class));
    }

    @Test
    @DisplayName("상품 삭제 - 상품 존재")
    public void testRemoveProduct_ExistingProduct() {
        Long productId = 1L;
        Product product = new Product(productId, "Product 1", 1000, "http://www.example.com/product1.jpg");

        when(productDao.findById(productId)).thenReturn(product);

        assertDoesNotThrow(() -> productService.remove(productId),
            "Expected no exception to be thrown");

        verify(productDao, times(1)).findById(productId);
        verify(cartItemDao, times(1)).deleteByProductId(productId);
        verify(productDao, times(1)).delete(productId);
    }

    @Test
    @DisplayName("상품 삭제 - 상품 미존재")
    public void testRemoveProduct_NonExistingProduct() {
        Long productId = 1L;

        when(productDao.findById(productId)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> productService.remove(productId),
            "Expected BadRequestException to be thrown");

        verify(productDao, times(1)).findById(productId);
        verify(cartItemDao, never()).deleteByProductId(anyLong());
        verify(productDao, never()).delete(anyLong());
    }
}
