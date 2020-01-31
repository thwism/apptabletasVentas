package ibzssoft.com.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ibzssoft.com.ishidamovile.R;

/**
 * Created by Ricardo on 7/27/2015.
 */
public class TabFragmentShop extends Fragment{

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
            View x =  inflater.inflate(R.layout.tab_layout,null);
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new AdapterTabShop(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                   }
        });
        return x;
    }


    class AdapterTabShop extends FragmentPagerAdapter {

        public AdapterTabShop(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
          Class fragmentClass;
          Fragment fragment;
          try{
              switch (position){
                  case 0 :
                      fragmentClass = CategoryFragment.class;
                      fragment = (Fragment) fragmentClass.newInstance();
                      return fragment;
                  case 1 :
                      fragmentClass = CategoryFragment.class;
                      fragment = (Fragment) fragmentClass.newInstance();
                      return fragment;
                  case 2 :
                      fragmentClass = CategoryFragment.class;
                      fragment = (Fragment) fragmentClass.newInstance();
                      return fragment;
              }
          }catch (Exception e){
              Toast.makeText(getActivity(),"No se puede cargar el fragmento",Toast.LENGTH_SHORT).show();
          }

        return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Cliente";
                case 1 :
                    return "Productos";
                case 2 :
                    return "Resumen";
            }
                return null;
        }
    }
}
