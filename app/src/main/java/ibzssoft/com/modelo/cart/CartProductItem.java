package ibzssoft.com.modelo.cart;

import com.google.gson.annotations.SerializedName;

public class CartProductItem {

    private long id;

    @SerializedName("product_id")
    private String productId;
    private int quantity;

    @SerializedName("total_price")
    private double totalItemPrice;

    @SerializedName("total_item_price_formatted")
    private String totalItemPriceFormatted;

    public CartProductItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public String getTotalItemPriceFormatted() {
        return totalItemPriceFormatted;
    }

    public void setTotalItemPriceFormatted(String totalItemPriceFormatted) {
        this.totalItemPriceFormatted = totalItemPriceFormatted;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartProductItem that = (CartProductItem) o;

        if (id != that.id) return false;
        if (productId != that.productId) return false;
        if (quantity != that.quantity) return false;
        if (Double.compare(that.totalItemPrice, totalItemPrice) != 0) return false;
        return totalItemPriceFormatted != null ? totalItemPriceFormatted.equals(that.totalItemPriceFormatted) : that.totalItemPriceFormatted == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + quantity;
        temp = Double.doubleToLongBits(totalItemPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (totalItemPriceFormatted != null ? totalItemPriceFormatted.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CartProductItem{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", totalItemPrice=" + totalItemPrice +
                ", totalItemPriceFormatted='" + totalItemPriceFormatted + '\'' +
                '}';
    }
}

