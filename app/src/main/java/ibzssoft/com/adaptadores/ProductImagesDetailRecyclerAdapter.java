package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Adapter handling list of product images.
 */
public class ProductImagesDetailRecyclerAdapter extends RecyclerView.Adapter<ProductImagesDetailRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<String> productImagesUrls;
    private LayoutInflater layoutInflater;
    private int screenWidth;
    /**
     * Creates an adapter that handles a list of product images.
     *
     */
    public ProductImagesDetailRecyclerAdapter(Context context, String id_item) {
        this.context = context;
        productImagesUrls = new ArrayList<>();
        cargarImagenes(id_item);
        /*Get Image SizeScreen*/
        WindowManager wm = (WindowManager) ((Activity)this.context).getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }
    public void cargarImagenes(String id_item){
       /*DBSistemaGestion helper= new DBSistemaGestion(this.context);
        Cursor cursor = helper.obtenerImagenItem(id_item);
        if(cursor.moveToFirst()){
            do{
                productImagesUrls.add(cursor.getString(cursor.getColumnIndex(Imagenes.FIELD_img)));
                productImagesUrls.add("http://static3.esoterismos.com/wp-content/uploads/2008/09/Sonar-con-una-cama-amplia-y-vacia-600x419.jpg");
                productImagesUrls.add("http://www.elcomercio.com/files/article_main/uploads/2016/01/13/5696b2914cb27.png");
            }while (cursor.moveToNext());
        }
        cursor.close();
        helper.close();*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String productImageUrl = getItem(position);
        holder.setPosition(position);
        Uri uri= Uri.parse(productImageUrl);

        int height;
        if (position == 1) {
            height = 150;
        } else {
            height = 300;
        }
        Picasso.with(context)
                .load(uri)
                .resize(screenWidth / 2, height)
                .centerCrop()
                .placeholder(R.drawable.placeholder_loading)
                .error(R.drawable.placeholder_loading)
                .into(holder.productImage);
    }

    private String getItem(int position) {
        return productImagesUrls.get(position);
    }

    @Override
    public int getItemCount() {
        return productImagesUrls.size();
    }

    /**
     * Add the product image url to the list.
     *
     * @param position        list position where item should be added.
     * @param productImageUrl image url to add.
     */
    public void add(int position, String productImageUrl) {
        productImagesUrls.add(position, productImageUrl);
        notifyItemInserted(position);
    }

    /**
     * Add the product image url at the end of the list.
     *
     * @param productImageUrl image url to add.
     */
    public void addLast(String productImageUrl) {
        productImagesUrls.add(productImagesUrls.size(), productImageUrl);
        notifyItemInserted(productImagesUrls.size());
    }

    /**
     * Clear list of product images.
     */
    public void clearAll() {
        int itemCount = productImagesUrls.size();

        if (itemCount > 0) {
            productImagesUrls.clear();
            notifyItemRangeRemoved(0, itemCount);
        }
    }


    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        int position;

        public ViewHolder(View v) {
            super(v);
            productImage = (ImageView) v.findViewById(R.id.ivItemGridImage);

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.support.v7.app.AlertDialog.Builder quitDialog
                            = new android.support.v7.app.AlertDialog.Builder(view.getContext());
                    quitDialog.setTitle("Deseas cambiar la imagen y almacenarla en el servidor?");

                    quitDialog.setPositiveButton("Salir", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }});

                    quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }});

                    quitDialog.show();
                }
            });
        }


        public void setPosition(int position) {
            this.position = position;
        }

    }
}