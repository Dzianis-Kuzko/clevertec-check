package ru.clevertec.check.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.core.dto.CreateProductDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.dao.api.IProductDao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductDao productDaoMock;
    @InjectMocks
    private ProductService productService;

    @Test
    void testGet() {
        long productId = 1L;

        ProductDTO expectedProduct = getProductDTO();

        when(productDaoMock.get(productId)).thenReturn(expectedProduct);

        ProductDTO actualProduct = productService.get(productId);

        assertEquals(expectedProduct, actualProduct);

    }

    @Test
    void testCreate() {

        CreateProductDTO createProductDTO = getCreateProductDTO();

        ProductDTO expectedProduct = getProductDTO();

        when(productDaoMock.create(createProductDTO)).thenReturn(expectedProduct);

        ProductDTO actualProduct = productService.create(createProductDTO);

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void testUpdate() {
        ProductDTO productDTO = getProductDTO();

        productService.update(productDTO);

        verify(productDaoMock).update(productDTO);
    }

    @Test
    void testDelete() {
        long productID = 1;

        productService.delete(productID);

        verify(productDaoMock).delete(productID);
    }

    private CreateProductDTO getCreateProductDTO() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setDescription("Milk");
        dto.setPrice(1.00);
        dto.setQuantityInStock(4);
        dto.setWholesaleProduct(true);
        return dto;
    }

    private ProductDTO getProductDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setId(1);
        dto.setDescription("Milk");
        dto.setPrice(1.0);
        dto.setQuantityInStock(4);
        dto.setWholesaleProduct(true);
        return dto;
    }


}