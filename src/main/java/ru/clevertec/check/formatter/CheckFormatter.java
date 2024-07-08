package ru.clevertec.check.formatter;

import ru.clevertec.check.core.CartProduct;
import ru.clevertec.check.core.Check;

import java.time.format.DateTimeFormatter;

public class CheckFormatter {
    public String formatCheckForCSV(Check check) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = check.getDate().format(dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = check.getTime().format(timeFormatter);

        sb.append("Date;Time\n" + formattedDate + ";" + formattedTime + "\n\n")
                .append("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");

        for (CartProduct cartProduct : check.getCartProducts()) {
            sb.append(String.format("%d;%s;%.2f$;%.2f$;%.2f$\n",
                    cartProduct.getQuantity(),
                    cartProduct.getDescription(),
                    cartProduct.getPrice(),
                    cartProduct.getDiscount(),
                    cartProduct.getTotalPrice()));
        }

        if (check.getDiscountCardNumber() != null) {
            sb.append("\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n" +
                    check.getDiscountCardNumber() + ";" +
                    check.getDiscountPercentage() + "%\n");
        }
        sb.append("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
        sb.append(String.format("%.2f$;%.2f$;%.2f$\n",
                check.getTotalPrice(),
                check.getTotalDiscount(),
                check.getTotalWithDiscount()));

        return sb.toString();
    }

    public String formatErrorForCsv(String ErrorMessage) {
        return "ERROR\n" + ErrorMessage;
    }
}
