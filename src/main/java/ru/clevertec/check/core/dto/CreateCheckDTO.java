package main.java.ru.clevertec.check.core.dto;

import java.util.Map;

public class CreateCheckDTO {
    private Map<Long, Integer> cartProducts;
    private String discountCardNumber;
    private double balanceDebitCard;

    public CreateCheckDTO() {

    }

    public CreateCheckDTO(Map<Long, Integer> cartProducts, String discountCardNumber, double balanceDebitCard) {
        this.cartProducts = cartProducts;
        this.discountCardNumber = discountCardNumber;
        this.balanceDebitCard = balanceDebitCard;
    }

    public Map<Long, Integer> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(Map<Long, Integer> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public String getDiscountCardNumber() {
        return discountCardNumber;
    }

    public void setDiscountCardNumber(String discountCardNumber) {
        this.discountCardNumber = discountCardNumber;
    }

    public double getBalanceDebitCard() {
        return balanceDebitCard;
    }

    public void setBalanceDebitCard(double balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateCheckDTO that = (CreateCheckDTO) o;

        if (Double.compare(balanceDebitCard, that.balanceDebitCard) != 0) return false;
        if (!cartProducts.equals(that.cartProducts)) return false;
        return discountCardNumber.equals(that.discountCardNumber);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = cartProducts.hashCode();
        result = 31 * result + discountCardNumber.hashCode();
        temp = Double.doubleToLongBits(balanceDebitCard);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "CreateCheckDTO{" +
                "cartProducts=" + cartProducts +
                ", discountCardNumber='" + discountCardNumber + '\'' +
                ", balanceDebitCard=" + balanceDebitCard +
                '}';
    }
}
