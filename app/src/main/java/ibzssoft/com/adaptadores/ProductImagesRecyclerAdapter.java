package ibzssoft.com.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.interfaces.ProductImagesRecyclerInterface;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.vista.Item;
import ibzssoft.com.views.ResizableImageViewHeight;

/**
 * Adapter handling list of product images.
 */
public class ProductImagesRecyclerAdapter extends RecyclerView.Adapter<ProductImagesRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final ProductImagesRecyclerInterface productImagesRecyclerInterface;
    private List<String> productImagesUrls;
    private LayoutInflater layoutInflater;
    Item item;
    /**
     * Creates an adapter that handles a list of product images.
     *
     * @param context                        activity context.
     * @param productImagesRecyclerInterface listener indicating events that occurred.
     */
    public ProductImagesRecyclerAdapter(Context context, Item item, ProductImagesRecyclerInterface productImagesRecyclerInterface) {
        this.context = context;
        this.item=item;
        this.productImagesRecyclerInterface = productImagesRecyclerInterface;
        productImagesUrls = new ArrayList<>();
        productImagesUrls.add(item.getRuta_img1());
        productImagesUrls.add(item.getRuta_img2());
        productImagesUrls.add(item.getRuta_img3());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_product_image, parent, false);
        return new ViewHolder(view, productImagesRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String productImageUrl = getItem(position);
        holder.setPosition(position);
        holder.setIdItem(item.getIdentificador());
        holder.setContext(context);
        Uri uri;
        if(productImageUrl!=null)
            uri= Uri.fromFile(new File(productImageUrl));
        else uri=null;
        Picasso.with(context)
                .load(uri)
                .fit().centerInside()
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
        ResizableImageViewHeight productImage;
        int position;
        String idItem;
        Context context;
        public ViewHolder(View v, final ProductImagesRecyclerInterface productImagesRecyclerInterface) {
            super(v);
            productImage = (ResizableImageViewHeight) v.findViewById(R.id.list_item_product_images_view);
            v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Seleccionar imagen desde");
                    builder.setItems(new CharSequence[] {"Camara", "Galeria"},
                            new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            MainActivity.getInstance().takeImage(idItem, position);
                                            break;
                                        case 1:
                                            MainActivity.getInstance().chooseImage(idItem, position);
                                            break;
                                    }
                                }
                            });

                    builder.show();
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productImagesRecyclerInterface != null)
                        productImagesRecyclerInterface.onImageSelected(v, position);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getIdItem() {
            return idItem;
        }

        public void setIdItem(String idItem) {
            this.idItem = idItem;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }
}