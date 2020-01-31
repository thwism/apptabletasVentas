package ibzssoft.com.adaptadores;

/**
 * Created by Usuario-pc on 06/04/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ibzssoft.com.ishidamovile.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class StaggeredGridLayoutAdapter extends CustomRecyclerViewAdapter {
    private Activity activity;
    private ArrayList<String> images;
    private int screenWidth;
    public StaggeredGridLayoutAdapter(Activity activity, ArrayList<String> images) {
        this.activity = activity;
        this.images = images;
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }
    @Override
    public CustomRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.image_item, parent, false);
        Holder dataObjectHolder = new Holder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Holder myHolder = (Holder) holder;
        int height;
        if (position == 1) {
            height = 150;
        } else {
            height = 300;
        }
        Picasso.with(activity)
                .load(images.get(position))
                .error(R.drawable.placeholder_loading)
                .placeholder(R.drawable.placeholder_loading)
                .resize(screenWidth / 2, height)
                .centerCrop()
                .into((myHolder.images));
    }


    @Override
    public int getItemCount() {
        return images.size();
    }
    public class Holder extends CustomRecycleViewHolder {
        private ImageView images;
        public Holder(View itemView) {
            super(itemView);
            images = (ImageView) itemView.findViewById(R.id.ivItemGridImage);
        }
    }
}