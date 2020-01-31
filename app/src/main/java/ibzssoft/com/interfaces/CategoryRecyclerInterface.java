package ibzssoft.com.interfaces;

import android.view.View;

import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.Product;
import ibzssoft.com.modelo.vista.Item;

public interface CategoryRecyclerInterface {

    void onProductSelected(View view, Item product);

}
