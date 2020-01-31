package ibzssoft.com.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.DrawerRecyclerAdapter;
import ibzssoft.com.adaptadores.DrawerSubmenuRecyclerAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.interfaces.DrawerRecyclerInterface;
import ibzssoft.com.interfaces.DrawerSubmenuRecyclerInterface;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.IVGrupo1;
import ibzssoft.com.modelo.IVGrupo2;
import ibzssoft.com.modelo.IVGrupo3;
import ibzssoft.com.modelo.IVGrupo4;
import ibzssoft.com.modelo.IVGrupo5;
import ibzssoft.com.modelo.IVGrupo6;
import ibzssoft.com.modelo.drawerMenu.DrawerItemCategory;
import ibzssoft.com.modelo.drawerMenu.DrawerItemPage;
import ibzssoft.com.modelo.drawerMenu.DrawerResponse;
import ibzssoft.com.storage.DBSistemaGestion;
import timber.log.Timber;

/**
 * Fragment handles the drawer menu.
 */
public class DrawerFragment extends Fragment {

    private static final int BANNERS_ID = -123;
    public static final String NULL_DRAWER_LISTENER_WTF = "Null drawer listener. WTF.";

    private ProgressBar drawerProgress;

    /**
     * Button to reload drawer menu content (used when content failed to load).
     */
    private Button drawerRetryBtn;
    /**
     * Indicates that menu is currently loading.
     */
    private boolean drawerLoading = false;

    /**
     * Listener indicating events that occurred on the menu.
     */
    private FragmentDrawerListener drawerListener;

    private ActionBarDrawerToggle mDrawerToggle;

    // Drawer top menu fields.
    private DrawerLayout mDrawerLayout;
    private RecyclerView drawerRecycler;
    private DrawerRecyclerAdapter drawerRecyclerAdapter;

    // Drawer sub menu fields
    private LinearLayout drawerSubmenuLayout;
    private TextView drawerSubmenuTitle;
    private DrawerSubmenuRecyclerAdapter drawerSubmenuRecyclerAdapter;
    //Drawer category primary
    private  int conf_category_default;
    private  String conf_category_tittle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);
        this.loadPreferences();
        drawerSubmenuLayout = (LinearLayout) layout.findViewById(R.id.drawer_submenu_layout);
        drawerSubmenuTitle = (TextView) layout.findViewById(R.id.drawer_submenu_title);
        drawerProgress = (ProgressBar) layout.findViewById(R.id.drawer_progress);

        drawerRetryBtn = (Button) layout.findViewById(R.id.drawer_retry_btn);
        drawerRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLoading)
                    getDrawerItems();
            }
        });

        prepareDrawerRecycler(layout);

        Button backBtn = (Button) layout.findViewById(R.id.drawer_submenu_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            private long mLastClickTime = 0;

            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                    return;
                mLastClickTime = SystemClock.elapsedRealtime();

                animateSubListHide();
            }
        });

        getDrawerItems();

        return layout;
    }
    /**
        Preparar preferencias
    */
    public void loadPreferences(){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getActivity());
        conf_category_default = Integer.parseInt(ext.get(getString(R.string.key_conf_carrito_categorias),"1"));
        switch (conf_category_default){
            case 1:this.conf_category_tittle=ext.get(getActivity().getString(R.string.key_g1),getActivity().getString(R.string.pref_etiqueta_grupo1_inventario_default));break;
            case 2:this.conf_category_tittle=ext.get(getActivity().getString(R.string.key_g2),getActivity().getString(R.string.pref_etiqueta_grupo2_inventario_default));break;
            case 3:this.conf_category_tittle=ext.get(getActivity().getString(R.string.key_g3),getActivity().getString(R.string.pref_etiqueta_grupo3_inventario_default));break;
            case 4:this.conf_category_tittle=ext.get(getActivity().getString(R.string.key_g4),getActivity().getString(R.string.pref_etiqueta_grupo4_inventario_default));break;
            case 5:this.conf_category_tittle=ext.get(getActivity().getString(R.string.key_g5),getActivity().getString(R.string.pref_etiqueta_grupo5_inventario_default));break;
            case 6:this.conf_category_tittle=ext.get(getActivity().getString(R.string.key_g6),getActivity().getString(R.string.pref_etiqueta_grupo6_inventario_default));break;
        }
    }
    /**
     * Prepare drawer menu content views, adapters and listeners.
     *
     * @param view fragment base view.
     */
    private void prepareDrawerRecycler(View view) {
        drawerRecycler = (RecyclerView) view.findViewById(R.id.drawer_recycler);
        drawerRecyclerAdapter = new DrawerRecyclerAdapter(getContext(), new DrawerRecyclerInterface() {
            @Override
            public void onCategorySelected(View v, DrawerItemCategory drawerItemCategory) {
                if (drawerItemCategory.getChildren() == null || drawerItemCategory.getChildren().isEmpty()) {
                    if (drawerListener != null) {
                        if (drawerItemCategory.getId() == BANNERS_ID)
                            drawerListener.onDrawerBannersSelected();
                        else
                            drawerListener.onDrawerItemCategorySelected(drawerItemCategory);
                        closeDrawerMenu();
                    } else {
                        Timber.e(new RuntimeException(), NULL_DRAWER_LISTENER_WTF);
                    }
                } else
                    animateSubListShow(drawerItemCategory);
            }

            @Override
            public void onPageSelected(View v, DrawerItemPage drawerItemPage) {
                if (drawerListener != null) {
                    drawerListener.onDrawerItemPageSelected(drawerItemPage);
                    closeDrawerMenu();
                } else {
                    Timber.e(new RuntimeException(), NULL_DRAWER_LISTENER_WTF);
                }
            }

            @Override
            public void onHeaderSelected() {
                if (drawerListener != null) {
                    drawerListener.onAccountSelected();
                    closeDrawerMenu();
                } else {
                    Timber.e(new RuntimeException(), NULL_DRAWER_LISTENER_WTF);
                }
            }
        });
        drawerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        drawerRecycler.setHasFixedSize(true);
        drawerRecycler.setAdapter(drawerRecyclerAdapter);

        RecyclerView drawerSubmenuRecycler = (RecyclerView) view.findViewById(R.id.drawer_submenu_recycler);
        drawerSubmenuRecyclerAdapter = new DrawerSubmenuRecyclerAdapter(new DrawerSubmenuRecyclerInterface() {
            @Override
            public void onSubCategorySelected(View v, DrawerItemCategory drawerItemCategory) {
                if (drawerListener != null) {
                    drawerListener.onDrawerItemCategorySelected(drawerItemCategory);
                    closeDrawerMenu();
                }
            }
        });
        drawerSubmenuRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        drawerSubmenuRecycler.setItemAnimator(new DefaultItemAnimator());
        drawerSubmenuRecycler.setHasFixedSize(true);
        drawerSubmenuRecycler.setAdapter(drawerSubmenuRecyclerAdapter);
    }

    /**
     * Base method for layout preparation. Also set a listener that will respond to events that occurred on the menu.
     *
     * @param drawerLayout   drawer layout, which will be managed.
     * @param toolbar        toolbar bundled with a side menu.
     * @param eventsListener corresponding listener class.
     */
    public void setUp(DrawerLayout drawerLayout, final Toolbar toolbar, FragmentDrawerListener eventsListener) {
        mDrawerLayout = drawerLayout;
        this.drawerListener = eventsListener;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.content_description_open_navigation_drawer, R.string.content_description_close_navigation_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
//                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerMenu();
            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    /**
     * When the drawer menu is open, close it. Otherwise open it.
     */
    public void toggleDrawerMenu() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }
    }

    /**
     * When the drawer menu is open, close it.
     */
    public void closeDrawerMenu() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * Check if drawer is open. If so close it.
     *
     * @return false if drawer was already closed
     */
    public boolean onBackHide() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            if (drawerSubmenuLayout.getVisibility() == View.VISIBLE)
                animateSubListHide();
            else
                mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    /**
     * Method invalidates a drawer menu header. It is used primarily on a login state change.
     */
    public void invalidateHeader() {
        if (drawerRecyclerAdapter != null) {
            Timber.d("Invalidate drawer menu header.");
            drawerRecyclerAdapter.notifyItemChanged(0);
        }
    }

    public DrawerResponse prepareDrawerReponse(){
        DrawerResponse response= new DrawerResponse();
        List<DrawerItemCategory> navigation= new ArrayList<>();
        try{
            DBSistemaGestion helper = new DBSistemaGestion(getContext());
            Cursor cursor=null;
            switch (conf_category_default){
                case 1:cursor=helper.consultarIVGrupo(IVGrupo1.TABLE_NAME);break;
                case 2:cursor=helper.consultarIVGrupo(IVGrupo2.TABLE_NAME);break;
                case 3:cursor=helper.consultarIVGrupo(IVGrupo3.TABLE_NAME);break;
                case 4:cursor=helper.consultarIVGrupo(IVGrupo4.TABLE_NAME);break;
                case 5:cursor=helper.consultarIVGrupo(IVGrupo5.TABLE_NAME);break;
                case 6:cursor=helper.consultarIVGrupo(IVGrupo6.TABLE_NAME);break;
            }
            if(cursor!=null){
                if(cursor.moveToFirst()){
                    do {
                        DrawerItemCategory item= new DrawerItemCategory();
                        item.setId(cursor.getInt(1));item.setType(cursor.getString(2));item.setName(cursor.getString(3));item.setNumgrupo(conf_category_default);
                        navigation.add(item);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
            helper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        response.setNavigation(navigation);
        return response;
    }

    private void getDrawerItems() {
        drawerLoading = true;
        drawerProgress.setVisibility(View.VISIBLE);
        drawerRetryBtn.setVisibility(View.GONE);
        try{
            DrawerResponse response = this.prepareDrawerReponse();
            drawerRecyclerAdapter.addDrawerItem(new DrawerItemCategory(BANNERS_ID, BANNERS_ID, this.conf_category_tittle));
            drawerRecyclerAdapter.addDrawerItemList(response.getNavigation());
            drawerRecyclerAdapter.addPageItemList(response.getPages());
            drawerRecyclerAdapter.notifyDataSetChanged();

            if (drawerListener != null)
                drawerListener.prepareSearchSuggestions(response.getNavigation());

            drawerLoading = false;
            if (drawerRecycler != null) drawerRecycler.setVisibility(View.VISIBLE);
            if (drawerProgress != null) drawerProgress.setVisibility(View.GONE);
        }catch (Exception e){

        }

    }

    private void animateSubListHide() {
        Animation slideAwayDisappear = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_away_disappear);
        final Animation slideAwayAppear = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_away_appear);
        slideAwayDisappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                drawerRecycler.setVisibility(View.VISIBLE);
                drawerRecycler.startAnimation(slideAwayAppear);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                drawerSubmenuLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        drawerSubmenuLayout.startAnimation(slideAwayDisappear);
    }

    private void animateSubListShow(DrawerItemCategory drawerItemCategory) {
        if (drawerItemCategory != null) {
            drawerSubmenuTitle.setText(drawerItemCategory.getName());
            drawerSubmenuRecyclerAdapter.changeDrawerItems(drawerItemCategory.getChildren());
            Animation slideInDisappear = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_disappear);
            final Animation slideInAppear = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_appear);
            slideInDisappear.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    drawerSubmenuLayout.setVisibility(View.VISIBLE);
                    drawerSubmenuLayout.startAnimation(slideInAppear);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    drawerRecycler.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            drawerRecycler.startAnimation(slideInDisappear);
        } else {
            Timber.e("Populate submenu with null category drawer item.");
        }
    }

    @Override
    public void onPause() {
        // Cancellation during onPause is needed because of app restarting during changing shop.
/*        MyApplication.getInstance().cancelPendingRequests(CONST.DRAWER_REQUESTS_TAG);
        if (drawerLoading) {
            if (drawerProgress != null) drawerProgress.setVisibility(View.GONE);
            if (drawerRetryBtn != null) drawerRetryBtn.setVisibility(View.VISIBLE);
            drawerLoading = false;
        }*/
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mDrawerLayout.removeDrawerListener(mDrawerToggle);
        super.onDestroy();
    }

    /**
     * Interface defining events initiated by {@link DrawerFragment}.
     */
    public interface FragmentDrawerListener {

        /**
         * Launch {@link }. If fragment is already launched nothing happen.
         */
        void onDrawerBannersSelected();

        /**
         * Launch {@link }.
         *
         * @param drawerItemCategory object specifying selected item in the drawer.
         */
        void onDrawerItemCategorySelected(DrawerItemCategory drawerItemCategory);

        /**
         * Launch {@link }, with downloadable content.
         *
         * @param drawerItemPage id of page for download and display. (Define in OpenShop server administration)
         */
        void onDrawerItemPageSelected(DrawerItemPage drawerItemPage);

        /**
         * Launch {@link }.
         */
        void onAccountSelected();

        /**
         * Prepare all search strings for search whisperer.
         *
         * @param navigation items for suggestions generating.
         */
        void prepareSearchSuggestions(List<DrawerItemCategory> navigation);
    }
}
