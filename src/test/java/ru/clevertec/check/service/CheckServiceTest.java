package ru.clevertec.check.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.core.CartProduct;
import ru.clevertec.check.core.Check;
import ru.clevertec.check.core.dto.CreateCheckDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.service.api.IDiscountCardService;
import ru.clevertec.check.service.api.IProductService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CheckServiceTest {
    @Mock
    private IProductService productServiceMock;
    @Mock
    private IDiscountCardService discountCardServiceMock;
    @InjectMocks
    private CheckService checkService;

    @Test
    void testCreateCheckSuccess() {
        CreateCheckDTO createCheckDTO = getCreateCheckDTO();
        DiscountCardDTO discountCardDTO = getDiscountCardDTO();

        Check expectedCheck = getCheck(true);

        Mockito.when(productServiceMock.get(13)).thenReturn(new ProductDTO(13, "Baguette 360g", 1.30, 10, true));
        Mockito.when(productServiceMock.get(9L)).thenReturn(new ProductDTO(9, "Packed bananas 1kg", 1.10, 25, true));
        Mockito.when(productServiceMock.get(4L)).thenReturn(new ProductDTO(4, "Packed potatoes 1kg", 1.47, 30, false));
        Mockito.when(productServiceMock.get(1L)).thenReturn(new ProductDTO(1, "Milk", 1.07, 10, true));

        Mockito.when(discountCardServiceMock.get("1111")).thenReturn(discountCardDTO);

        Check actualCheck = checkService.createCheck(createCheckDTO);

        List<CartProduct> expectedCartProducts = expectedCheck.getCartProducts();
        List<CartProduct> actualCartProducts = actualCheck.getCartProducts();

        expectedCartProducts.sort(Comparator.comparing(CartProduct::getProductId));
        actualCartProducts.sort(Comparator.comparing(CartProduct::getProductId));

        assertEquals(expectedCartProducts.toString(), actualCartProducts.toString());
        assertEquals(expectedCheck.getDiscountCardNumber(), actualCheck.getDiscountCardNumber());
        assertEquals(expectedCheck.getDiscountPercentage(), actualCheck.getDiscountPercentage());
        assertEquals(expectedCheck.getTotalPrice(), actualCheck.getTotalPrice());
        assertEquals(expectedCheck.getTotalDiscount(), actualCheck.getTotalDiscount());
        assertEquals(expectedCheck.getTotalWithDiscount(), actualCheck.getTotalWithDiscount());

    }

    private CreateCheckDTO getCreateCheckDTO() {
        CreateCheckDTO dto = new CreateCheckDTO();
        dto.setCartProducts(Map.of(13l, 5, 9l, 1, 4l, 30, 1l, 6));
        dto.setDiscountCardNumber("1111");
        dto.setBalanceDebitCard(100.25);

        return dto;
    }

    private Check getCheck(boolean withDiscountCard) {
        ProductDTO dto1 = new ProductDTO(13, "Baguette 360g", 1.30, 10, true);
        ProductDTO dto2 = new ProductDTO(9, "Packed bananas 1kg", 1.10, 25, true);
        ProductDTO dto3 = new ProductDTO(4, "Packed potatoes 1kg", 1.47, 30, false);
        ProductDTO dto4 = new ProductDTO(1, "Milk", 1.07, 10, true);

        Check.CheckBuilder checkBuilder = new Check.CheckBuilder();

        if (withDiscountCard) {
            DiscountCardDTO discountCardDTO = new DiscountCardDTO(1, "1111", 3);
            checkBuilder.withDiscountCard(discountCardDTO);
        }

        checkBuilder.addProduct(dto1, 5);
        checkBuilder.addProduct(dto2, 1);
        checkBuilder.addProduct(dto3, 30);
        checkBuilder.addProduct(dto4, 6);

        return checkBuilder.build();
    }

    private DiscountCardDTO getDiscountCardDTO(){
        DiscountCardDTO dto = new DiscountCardDTO();
        dto.setId(1);
        dto.setNumber("1111");
        dto.setDiscountAmount(3);
        return dto;
    }

}