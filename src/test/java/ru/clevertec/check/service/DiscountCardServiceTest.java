package ru.clevertec.check.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.dao.api.IDiscountCardDao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountCardServiceTest {
    @Mock
    private IDiscountCardDao discountCardDaoMock;
    @InjectMocks
    private DiscountCardService service;

    @Test
    void testGetById() {
        long discountCardId = 1L;

        DiscountCardDTO expectedDC = getDiscountCardDTO();

        when(discountCardDaoMock.get(discountCardId)).thenReturn(expectedDC);

        DiscountCardDTO actualDC = service.get(discountCardId);

        assertEquals(expectedDC, actualDC);
    }


    @Test
    void testCreate() {
        CreateDiscountCardDTO createDiscountCardDTO = getCreateDiscountCardDTO();

        DiscountCardDTO expectedDC = getDiscountCardDTO();

        when(discountCardDaoMock.create(createDiscountCardDTO)).thenReturn(expectedDC);

        DiscountCardDTO actualDC = service.create(createDiscountCardDTO);

        assertEquals(expectedDC, actualDC);
    }

    @Test
    void testUpdate() {
        DiscountCardDTO dto = getDiscountCardDTO();

        service.update(dto);

        verify(discountCardDaoMock).update(dto);
    }

    @Test
    void testDelete() {
        long discountCardID = 1;

        service.delete(discountCardID);

        verify(discountCardDaoMock).delete(discountCardID);
    }

    @Test
    void testGetByNumber() {
        String discountCardNumber = "1111";

        service.get(discountCardNumber);

        verify(discountCardDaoMock).get(discountCardNumber);
    }

    private DiscountCardDTO getDiscountCardDTO() {
        DiscountCardDTO dto = new DiscountCardDTO();
        dto.setId(1);
        dto.setNumber("1111");
        dto.setDiscountAmount(3);
        return dto;
    }


    private CreateDiscountCardDTO getCreateDiscountCardDTO() {
        CreateDiscountCardDTO dto = new CreateDiscountCardDTO();
        dto.setNumber("1111");
        dto.setDiscountAmount(3);
        return dto;
    }

}