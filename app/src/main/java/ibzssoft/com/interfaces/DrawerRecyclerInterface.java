package ibzssoft.com.interfaces;

import android.view.View;

import ibzssoft.com.modelo.drawerMenu.DrawerItemCategory;
import ibzssoft.com.modelo.drawerMenu.DrawerItemPage;

public interface DrawerRecyclerInterface {

    void onCategorySelected(View v, DrawerItemCategory drawerItemCategory);

    void onPageSelected(View v, DrawerItemPage drawerItemPage);

    void onHeaderSelected();
}
