package ru.clevertec.check.core.dto;

public class CreateDiscountCardDTO {
    private String number;
    private int discountAmount;

    public CreateDiscountCardDTO() {
    }

    public CreateDiscountCardDTO(String number, int discountAmount) {
        this.number = number;
        this.discountAmount = discountAmount;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateDiscountCardDTO that = (CreateDiscountCardDTO) o;

        if (discountAmount != that.discountAmount) return false;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + discountAmount;
        return result;
    }

    @Override
    public String toString() {
        return "CreateDiscountCardDTO{" +
                "number='" + number + '\'' +
                ", discountAmount=" + discountAmount +
                '}';
    }
}
