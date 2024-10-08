package ru.clevertec.check.core.dto;

public class ProductDTO {
    private long id;
    private String description;
    private double price;

    private int quantityInStock;

    private boolean isWholesaleProduct;

    public ProductDTO() {
    }

    public ProductDTO(long id, String description, double price, int quantityInStock, boolean isWholesaleProduct) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.isWholesaleProduct = isWholesaleProduct;
    }

    public ProductDTO(CreateProductDTO createProductDTO) {
        this.description = createProductDTO.getDescription();
        this.price = createProductDTO.getPrice();
        this.quantityInStock = createProductDTO.getQuantityInStock();
        this.isWholesaleProduct = createProductDTO.isWholesaleProduct();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public boolean isWholesaleProduct() {
        return isWholesaleProduct;
    }

    public void setWholesaleProduct(boolean wholesaleProduct) {
        isWholesaleProduct = wholesaleProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDTO that = (ProductDTO) o;

        if (id != that.id) return false;
        if (Double.compare(price, that.price) != 0) return false;
        if (quantityInStock != that.quantityInStock) return false;
        if (isWholesaleProduct != that.isWholesaleProduct) return false;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + description.hashCode();
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + quantityInStock;
        result = 31 * result + (isWholesaleProduct ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                ", isWholesaleProduct=" + isWholesaleProduct +
                '}';
    }
}
