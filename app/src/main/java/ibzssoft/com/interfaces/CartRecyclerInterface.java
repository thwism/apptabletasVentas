package ibzssoft.com.interfaces;

import ibzssoft.com.modelo.cart.CartDiscountItem;
import ibzssoft.com.modelo.cart.CartProductItem;

public interface CartRecyclerInterface {

    void onProductUpdate(CartProductItem cartProductItem);

    void onProductDelete(CartProductItem cartProductItem);

    void onDiscountDelete(CartDiscountItem cartDiscountItem);

    void onProductSelect(String productId);

}
