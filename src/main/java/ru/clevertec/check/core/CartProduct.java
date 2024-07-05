package main.java.ru.clevertec.check.core;

import main.java.ru.clevertec.check.core.dto.ProductDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartProduct {
    private static final int QUANTITY_OF_PRODUCT_FOR_WHOLESALE_DISCOUNT = 5;
    private static final int WHOLESALE_DISCOUNT = 10;
    private long productId;
    private int quantity;
    private String description;
    private double price;
    private double totalPrice;
    private double discount;

    private CartProduct(CartProductBuilder cartProductBuilder) {
        this.productId = cartProductBuilder.productId;
        this.description = cartProductBuilder.description;
        this.quantity = cartProductBuilder.quantity;
        this.price = cartProductBuilder.price;
        this.totalPrice = cartProductBuilder.totalPrice;
        this.discount = cartProductBuilder.discount;
    }

    public long getProductId() {
        return productId;
    }


    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    @Override
    public String toString() {
        return "CartProduct{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", discount=" + discount +
                '}';
    }

    public static class CartProductBuilder {
        private long productId;
        private int quantity;
        private String description;
        private double price;
        private double totalPrice;
        private int discountAmount;
        private double discount;

        public CartProductBuilder(ProductDTO productDTO, int quantity) {
            this.productId = productDTO.getId();
            this.description = productDTO.getDescription();
            this.quantity = quantity;
            this.price = productDTO.getPrice();

            this.totalPrice = BigDecimal.valueOf(this.quantity)
                    .multiply(BigDecimal.valueOf(this.price))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            this.discountAmount = productDTO.isWholesaleProduct()
                    &&
                    this.quantity >= QUANTITY_OF_PRODUCT_FOR_WHOLESALE_DISCOUNT ? WHOLESALE_DISCOUNT : 0;
        }

        public CartProductBuilder setDiscountAmount(int discountAmount) {
            if (this.discountAmount != WHOLESALE_DISCOUNT) {
                this.discountAmount = discountAmount;
            }
            return this;
        }

        private void calculateDiscount() {
            BigDecimal discountAmountBD = BigDecimal.valueOf(this.discountAmount);
            BigDecimal totalPriceBD = BigDecimal.valueOf(this.totalPrice);
            BigDecimal discountBD = totalPriceBD.multiply(discountAmountBD).divide(new BigDecimal(100));
            BigDecimal roundedDiscountBD = discountBD.setScale(2, RoundingMode.HALF_UP);
            this.discount = roundedDiscountBD.doubleValue();
        }

        public CartProduct build() {
            calculateDiscount();
            return new CartProduct(this);
        }
    }

}
