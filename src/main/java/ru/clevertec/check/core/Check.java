package main.java.ru.clevertec.check.core;

import main.java.ru.clevertec.check.core.dto.DiscountCardDTO;
import main.java.ru.clevertec.check.core.dto.ProductDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Check {
    private LocalDate date;
    private LocalTime time;
    private List<CartProduct> cartProducts;
    private String discountCardNumber;
    private int discountPercentage;
    private double totalPrice;
    private double totalDiscount;
    private double totalWithDiscount;

    private Check(CheckBuilder checkBuilder) {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.cartProducts = checkBuilder.cartProducts;
        this.discountCardNumber = checkBuilder.discountCardNumber;
        this.discountPercentage = checkBuilder.discountPercentage;
        this.totalPrice = checkBuilder.totalPrice;
        this.totalDiscount = checkBuilder.totalDiscount;
        this.totalWithDiscount = checkBuilder.totalWithDiscount;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public String getDiscountCardNumber() {
        return discountCardNumber;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public double getTotalWithDiscount() {
        return totalWithDiscount;
    }


    @Override
    public String toString() {
        return "Check{" +
                "date=" + date +
                ", time=" + time +
                ", cartProducts=" + cartProducts +
                ", discountCardNumber='" + discountCardNumber + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", totalPrice=" + totalPrice +
                ", totalDiscount=" + totalDiscount +
                ", totalWithDiscount=" + totalWithDiscount +
                '}';
    }

    public static class CheckBuilder {
        private List<CartProduct> cartProducts = new ArrayList<>();
        private String discountCardNumber;
        private int discountPercentage;
        private double totalPrice;
        private double totalDiscount;
        private double totalWithDiscount;

        public CheckBuilder addProduct(ProductDTO productDTO, int quantity) {
            CartProduct cartProduct = new CartProduct.CartProductBuilder(productDTO, quantity)
                    .setDiscountAmount(this.discountPercentage)
                    .build();
            cartProducts.add(cartProduct);
            return this;


        }

        public CheckBuilder withDiscountCard(DiscountCardDTO discountCardDTO) {
            this.discountCardNumber = discountCardDTO.getNumber();
            this.discountPercentage = discountCardDTO.getDiscountAmount();
            return this;
        }

        public CheckBuilder withoutDiscountCard() {
            this.discountCardNumber = null;
            this.discountPercentage = 0;
            return this;
        }


        public Check build() {
            calculateTotalPrice();
            calculateTotalDiscount();
            calculateTotalWithDiscount();
            return new Check(this);
        }

        private void calculateTotalPrice() {
            BigDecimal totalPriceBD = new BigDecimal(0);
            for (CartProduct cartProduct : cartProducts) {
                BigDecimal cartPTotalPriceBD = BigDecimal.valueOf(cartProduct.getTotalPrice());
                totalPriceBD = totalPriceBD.add(cartPTotalPriceBD);
            }

            this.totalPrice = totalPriceBD.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }

        private void calculateTotalDiscount() {
            BigDecimal totalDiscountBD = new BigDecimal(0);
            for (CartProduct cartProduct : cartProducts) {
                BigDecimal cartPTotalDiscountBD = BigDecimal.valueOf(cartProduct.getDiscount());
                totalDiscountBD = totalDiscountBD.add(cartPTotalDiscountBD);
            }

            this.totalDiscount = totalDiscountBD
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        private void calculateTotalWithDiscount() {
            BigDecimal totalPriceBD = BigDecimal.valueOf(this.totalPrice);
            BigDecimal totalDiscountBD = BigDecimal.valueOf(this.totalDiscount);
            this.totalWithDiscount = totalPriceBD.subtract(totalDiscountBD)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

    }

}
