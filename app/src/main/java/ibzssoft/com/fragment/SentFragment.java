package ibzssoft.com.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ibzssoft.com.ishidamovile.R;

/**
 * Created by Ratan on 7/29/2015.
 */
public class SentFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // this.extraerParametros();
        return inflater.inflate(R.layout.sent_layout,null);
    }


}
