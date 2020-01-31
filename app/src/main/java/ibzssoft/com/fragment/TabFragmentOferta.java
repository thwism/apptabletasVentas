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
import ibzssoft.com.paginas.TabOfertaAll;

/**
 * Created by Ricardo on 7/27/2015.
 */
public class TabFragmentOferta extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;
    public static String catalogo="OFERTA";
    /*
        Parametros
     */
    private String ip,port,url,vendedor,grupo,empresa,accesos,lineas,bodegas,impuestos,usuario;
    private int numdec,opcion;
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
        viewPager.setAdapter(new AdapterTabPedido(getChildFragmentManager()));

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
        this.extraerParametros();
        return x;
    }



    public void extraerParametros(){
        this.vendedor = getArguments().getString("vendedor");
        this.ip= getArguments().getString("ip");
        this.port= getArguments().getString("port");
        this.url= getArguments().getString("url");
        this.empresa= getArguments().getString("empresa");
        this.grupo= getArguments().getString("grupo");
        this.accesos= getArguments().getString("accesos");
        this.lineas= getArguments().getString("lineas");
        this.bodegas= getArguments().getString("bodegas");
        this.impuestos= getArguments().getString("impuestos");
        this.numdec= getArguments().getInt("numdec");
        this.opcion= getArguments().getInt("opcion");
        this.usuario= getArguments().getString("usuario");
    }


    class AdapterTabPedido extends FragmentPagerAdapter {

        public AdapterTabPedido(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            try{
                Class fragmentClass;
                Fragment fragment;
                switch (position) {
                    case 0:
                        fragmentClass = TabOfertaAll.class;
                        fragment = (Fragment) fragmentClass.newInstance();
                        fragment.setArguments(crearParametros());
                        return fragment;
                    case 1:
                        fragmentClass = TabOfertaAll.class;
                        fragment = (Fragment) fragmentClass.newInstance();
                        fragment.setArguments(crearParametros());
                        return fragment;
                    case 2:
                        fragmentClass = TabOfertaAll.class;
                        fragment = (Fragment) fragmentClass.newInstance();
                        fragment.setArguments(crearParametros());
                        return fragment;
                }
            }catch (Exception e){
                Toast.makeText(getActivity(),"Error creando fragmento",Toast.LENGTH_SHORT).show();
            }

        return null;
        }
        public Bundle crearParametros(){
            Bundle bundle= new Bundle();
            bundle.putString("vendedor",vendedor);
            bundle.putString("ip",ip);
            bundle.putString("port",port);
            bundle.putString("url",url);
            bundle.putString("empresa",empresa);
            bundle.putString("grupo",grupo);
            bundle.putString("accesos",accesos);
            bundle.putString("lineas",lineas);
            bundle.putString("bodegas",bodegas);
            bundle.putString("impuestos",impuestos);
            bundle.putInt("numdec",numdec);
            bundle.putInt("opcion",opcion);
            bundle.putString("usuario",usuario);
            bundle.putString("catalogo",catalogo);
            return bundle;
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
                    return "Todos";
                case 1 :
                    return "Enviados";
                case 2 :
                    return "No Enviados";
            }
                return null;
        }
    }

}
