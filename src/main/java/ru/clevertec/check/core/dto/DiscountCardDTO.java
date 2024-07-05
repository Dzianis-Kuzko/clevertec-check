package main.java.ru.clevertec.check.core.dto;

public class DiscountCardDTO {
    private long id;
    private String number;
    private int discountAmount;

    public DiscountCardDTO() {
    }

    public DiscountCardDTO(long id, String number, int discountAmount) {
        this.id = id;
        this.number = number;
        this.discountAmount = discountAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

        DiscountCardDTO that = (DiscountCardDTO) o;

        if (id != that.id) return false;
        if (discountAmount != that.discountAmount) return false;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + number.hashCode();
        result = 31 * result + discountAmount;
        return result;
    }

    @Override
    public String toString() {
        return "DiscountCardDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", discountAmount=" + discountAmount +
                '}';
    }
}
