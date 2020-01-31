package ibzssoft.com.ishidamovile;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.dialogs.ProductFragment;
import ibzssoft.com.fragment.CartFragment;
import ibzssoft.com.fragment.DrawerFragment;
import ibzssoft.com.modelo.Shop;
import ibzssoft.com.modelo.cart.CartProductItem;
import ibzssoft.com.modelo.drawerMenu.DrawerItemCategory;
import ibzssoft.com.modelo.drawerMenu.DrawerItemPage;
import ibzssoft.com.fragment.CategoryFragment;
import timber.log.Timber;

public class ShoppingCar extends AppCompatActivity implements DrawerFragment.FragmentDrawerListener{

    public static final String MSG_MAIN_ACTIVITY_INSTANCE_IS_NULL = "MainActivity instance is null.";
    private static ShoppingCar mInstance = null;

    /**
     * Reference tied drawer menu, represented as fragment.
     */
    public DrawerFragment drawerFragment;
    /**
     * Reference view showing number of products in shopping cart.
     */
    private TextView cartCountView;
    /**
     * Reference number of products in shopping cart.
     */
    private int cartCountNotificationValue = CONST.DEFAULT_EMPTY_ID;
    // Fields used in searchView.
    private SimpleCursorAdapter searchSuggestionsAdapter;
    private ArrayList<String> searchSuggestionsList;

    private Shop params;
    /***
     * Lista para archivar los items registrados
     */
    private List<CartProductItem> productos;
    /**
     * Return MainActivity instance. Null if activity doesn't exist.
     *
     * @return activity instance.
     */
    private static synchronized ShoppingCar getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        setContentView(R.layout.activity_shopping_car);
        // Prepare toolbar and navigation drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            Timber.e(new RuntimeException(), "GetSupportActionBar returned null.");
        }
        params = (Shop) getIntent().getSerializableExtra("shop");
        productos = new ArrayList<>();
        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.main_navigation_drawer_fragment);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.main_drawer_layout), toolbar, this);
    }

    /**
     * Method create new {@link } with defined search query.
     *
     * @param searchQuery text used for products search.
     */
    private void onSearchSubmitted(String searchQuery) {
        //clearBackStack();
        Timber.d("Called onSearchSubmitted with text: %s", searchQuery);
        System.out.println("query: "+searchQuery);
        //Fragment fragment = Inse.newInstance(searchQuery);
        //replaceFragment(fragment, CategoryFragment.class.getSimpleName());
    }

    @Override
    public void onDrawerBannersSelected() {
        clearBackStack();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_content_frame);
        /*if (f == null || !(f instanceof BannersFragment)) {
            Fragment fragment = new BannersFragment();
            replaceFragment(fragment, BannersFragment.class.getSimpleName());
        } else {
            Timber.d("Banners already displayed.");
        }*/
    }

    @Override
    public void onDrawerItemCategorySelected(DrawerItemCategory drawerItemCategory) {
        clearBackStack();
        drawerItemCategory.setPrecios(params.getPrecios());
        Fragment fragment = CategoryFragment.newInstance(drawerItemCategory);
        replaceFragment(fragment,CategoryFragment.class.getSimpleName());
    }

    @Override
    public void onDrawerItemPageSelected(DrawerItemPage drawerItemPage) {
        clearBackStack();
        //Fragment fragment = PageFragment.newInstance(drawerItemPage.getId());
        //replaceFragment(fragment, PageFragment.class.getSimpleName());
    }

    @Override
    public void onAccountSelected() {
        //AccountFragment fragment = new AccountFragment();
        //replaceFragment(fragment, AccountFragment.class.getSimpleName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping, menu);
        // Prepare search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            prepareSearchView(searchItem);
        }
        // Prepare cart count info
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(cartItem, R.layout.action_icon_shopping_cart);
        View view = MenuItemCompat.getActionView(cartItem);
        cartCountView = (TextView) view.findViewById(R.id.shopping_cart_notify);
        showNotifyCount(cartCountNotificationValue);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartSelected();
            }
        });
        if (cartCountNotificationValue == CONST.DEFAULT_EMPTY_ID) {
            // If first cart count check, then sync server cart data.
            getCartCount(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_cart) {
            onCartSelected();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void prepareSearchSuggestions(List<DrawerItemCategory> navigation) {

    }
    /**
     * Method creates fragment transaction and replace current fragment with new one.
     *
     * @param newFragment    new fragment used for replacement.
     * @param transactionTag text identifying fragment transaction.
     */
    private void replaceFragment(Fragment newFragment, String transactionTag) {
        if (newFragment != null) {
            FragmentManager frgManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = frgManager.beginTransaction();
            //fragmentTransaction.setAllowOptimization(false);
            fragmentTransaction.addToBackStack(transactionTag);
            fragmentTransaction.replace(R.id.main_content_frame, newFragment).commit();
            frgManager.executePendingTransactions();
        } else {
            Timber.e(new RuntimeException(), "Replace fragments with null newFragment parameter.");
        }
    }


    /**
     * Method clear fragment backStack (back history). On bottom of stack will remain Fragment added by {@link ()}.
     */
    private void clearBackStack() {
        Timber.d("Clearing backStack");
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            if (BuildConfig.DEBUG) {
                for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                    Timber.d("BackStack content_%d= id: %d, name: %s", i, manager.getBackStackEntryAt(i).getId(), manager.getBackStackEntryAt(i).getName());
                }
            }
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        Timber.d("backStack cleared.");
//        TODO maybe implement own fragment backStack handling to prevent banner fragment recreation during clearing.
//        http://stackoverflow.com/questions/12529499/problems-with-android-fragment-back-stack
    }
    /**
     * Refresh notification number of products in shopping cart.
     * Create action only if called from fragment attached to MainActivity.
     */
    public static void updateCartCountNotification(CartProductItem detalle) {
        ShoppingCar instance = ShoppingCar.getInstance();
        if (instance != null) {
            if(instance.validarItemIngresado(instance.productos, detalle)){
                instance.cambiarCantidadItemIngresado(instance.productos, detalle);
            }else instance.productos.add(detalle);
            instance.getCartCount(true);
        } else {
            Timber.e(MSG_MAIN_ACTIVITY_INSTANCE_IS_NULL);
        }
    }

    public boolean validarItemIngresado(List<CartProductItem> items, CartProductItem detalle){
        for(int i=0; i<items.size(); i++){
            if(detalle.getProductId().equals(items.get(i).getProductId()))
                return true;
        }
        return false;
    }

    public void cambiarCantidadItemIngresado(List<CartProductItem> items, CartProductItem detalle){
        for(int i=0; i<items.size(); i++){
            if(detalle.getProductId().equals(items.get(i).getProductId()))
                items.get(i).setQuantity(items.get(i).getQuantity()+detalle.getQuantity());
        }
    }

    public int numeroItemsIngresados(List<CartProductItem> items){
        int counter = 0;
        for(CartProductItem item: items){
            counter+=item.getQuantity();
        }
        return counter;
    }

    /**
     * Loads cart count from server.
     *
     * @param initialize if true, then server run cart synchronization . Useful during app starts.
     */
    private void getCartCount(boolean initialize) {
        Timber.d("Obtaining cart count.");
        if (cartCountView != null) {
                // If cart count is loaded for the first time, we need to load whole cart because of synchronization.
                if (initialize) {
                    if(this.productos!=null && this.productos.size()>0){
                        showNotifyCount(numeroItemsIngresados(this.productos));
                    }else{
                        showNotifyCount(0);
                    }
                }
        }
    }
    /**
     * If user is logged in then {@link } is launched . Otherwise is showed a login dialog.
     */
    public void onCartSelected() {
        Bundle bundle = new Bundle();
        Gson gson= new Gson();
        String jsonList = gson.toJson(productos);
        bundle.putString("productos",jsonList);
        launchUserSpecificFragment(new CartFragment(), CartFragment.class.getSimpleName(), bundle);
    }
    /**
     * Check if user is logged in. If so then start defined fragment, otherwise show login dialog.
     *
     * @param fragment       fragment to launch.
     * @param transactionTag text identifying fragment transaction.
     */
    private void launchUserSpecificFragment(Fragment fragment, String transactionTag, Bundle bundle) {
        fragment.setArguments(bundle);
       replaceFragment(fragment, transactionTag);
    }
    /**
     * Update actionBar title.
     * Create action only if called from fragment attached to MainActivity.
     */
    public static void setActionBarTitle(String title) {
        ShoppingCar instance = ShoppingCar.getInstance();
        if (instance != null) {
            // TODO want different toolbar text font?
//            SpannableString s = new SpannableString(title);
//            s.setSpan(new TypefaceSpan("sans-serif-light"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            instance.setTitle(s);
            instance.setTitle(title);
        } else {
            Timber.e(MSG_MAIN_ACTIVITY_INSTANCE_IS_NULL);
        }
    }

    /**
     * Method display cart count notification. Cart count notification remains hide if cart count is negative number.
     *
     * @param newCartCount cart count to show.
     */
    private void showNotifyCount(int newCartCount) {
        cartCountNotificationValue = newCartCount;
        Timber.d("Update cart count notification: %d", cartCountNotificationValue);
        if (cartCountView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (cartCountNotificationValue != 0 && cartCountNotificationValue != CONST.DEFAULT_EMPTY_ID) {
                        cartCountView.setText(getString(R.string.format_number, cartCountNotificationValue));
                        cartCountView.setVisibility(View.VISIBLE);
                    } else {
                        cartCountView.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            Timber.e("Cannot update cart count notification. Cart count view is null.");
        }
    }
    /**
     * Launch {@link CategoryFragment}.
     *
     * @param productId id of product for display.
     */
    public void onProductSelected(String productId) {
        Fragment fragment = ProductFragment.newInstance(productId,params.getPrecios(), params.getClienteid());
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            fragment.setReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
        }
        replaceFragment(fragment, ProductFragment.class.getSimpleName());
    }

    public boolean onProductDeleted(String productId) {
        for(int i= 0 ; i<this.productos.size(); i++){
            if(productos.get(i).getProductId().equals(productId)){
                productos.remove(i);
                getCartCount(true);
                return true;
            }
        }
        return false;
    }

    public boolean onProductUpdate(String productId, int newQuantity) {
        for(int i= 0 ; i<this.productos.size(); i++){
            if(productos.get(i).getProductId().equals(productId)){
                productos.get(i).setQuantity(newQuantity);
                getCartCount(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Prepare toolbar search view. Invoke search suggestions and handle search queries.
     *
     * @param searchItem corresponding menu item.
     */
    private void prepareSearchView(@NonNull final MenuItem searchItem) {
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        SearchManager searchManager = (SearchManager) ShoppingCar.this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ShoppingCar.this.getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                System.out.println("text: "+newText );
                Timber.d("Search query text changed to: %s", newText);
                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                // Submit search query and hide search action view.
                onSearchSubmitted(query);
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                return true;
            }
        };

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                // Submit search suggestion query and hide search action view.
                MatrixCursor c = (MatrixCursor) searchSuggestionsAdapter.getItem(position);
                onSearchSubmitted(c.getString(1));
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                return true;
            }
        });
        searchView.setOnQueryTextListener(queryTextListener);
    }


}
