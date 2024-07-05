package main.java.ru.clevertec.check.dao.file;

import main.java.ru.clevertec.check.core.dto.DiscountCardDTO;
import main.java.ru.clevertec.check.dao.api.IDiscountCardDao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DiscountCardFileDao implements IDiscountCardDao {
    private String filePath;

    public DiscountCardFileDao(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public DiscountCardDTO get(String number) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                String cardNumber = values[1];
                if (cardNumber.equals(number)) {
                    long id = Integer.parseInt(values[0]);
                    int discountAmount = Integer.parseInt(values[2]);

                    return new DiscountCardDTO(id, number, discountAmount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
