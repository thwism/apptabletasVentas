package ibzssoft.com.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.interfaces.CategoryRecyclerInterface;
import ibzssoft.com.modelo.vista.Item;

/**
 * Adapter handling list of product items.
 */
public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolderProducto> {

    private final Context context;
    private List<Item> mModels;
    private LayoutInflater layoutInflater;
    private String precios;
    private final CategoryRecyclerInterface categoryRecyclerInterface;

    public ProductsRecyclerAdapter(Context context, List<Item> models,String precios,CategoryRecyclerInterface categoryRecyclerInterface) {
        this.context = context;
        mModels = new ArrayList<>(models);
        this.precios=precios;
        this.categoryRecyclerInterface = categoryRecyclerInterface;
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }
    public void animateTo(List<Item> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Item> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final Item model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Item> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Item model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<Item> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Item model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public Item removeItem(int position) {
        final Item model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Item model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Item model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setFilter(List<Item> ivInventarios) {
        mModels.clear();
        mModels.addAll(ivInventarios);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolderProducto onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_products, parent, false);
        return new ViewHolderProducto(view, categoryRecyclerInterface);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolderProducto holder, int position) {
        final Item model = mModels.get(position);
        holder.bindContent(model);
        holder.productNameTV.setText(holder.product.getDescripcion());
        for(String price: precios.split(",")){
            int opc= Integer.parseInt(price);
            switch (opc){
                case 1:holder.productPriceTV.setText("$"+redondearNumero(holder.product.getPrecio1())); break;
                case 2:holder.productPriceTV.setText("$"+redondearNumero(holder.product.getPrecio2())); break;
                case 3:holder.productPriceTV.setText("$"+redondearNumero(holder.product.getPrecio3())); break;
                case 4:holder.productPriceTV.setText("$"+redondearNumero(holder.product.getPrecio4())); break;
                case 5:holder.productPriceTV.setText("$"+redondearNumero(holder.product.getPrecio5())); break;
                case 6:holder.productPriceTV.setText("$"+redondearNumero(holder.product.getPrecio6())); break;
                case 7:holder.productPriceTV.setText("$"+redondearNumero(holder.product.getPrecio7())); break;
            }
        }

        Uri uri;
        if(holder.product.getRuta_img1()!=null){
            uri= Uri.fromFile(new File(holder.product.getRuta_img1()));
        }
        else uri=null;
        Picasso.with(context).load(uri)
                .fit().centerInside()
                .placeholder(R.drawable.placeholder_loading)
                .error(R.drawable.placeholder_loading)
                .into(holder.productImage);
    }

    public String redondearNumero(double numero) {
        String clave = "0.00";
        DecimalFormat formateador = new DecimalFormat(clave);
        return formateador.format(numero).replace(",", ".");
    }
    // Provide a reference to the views for each data item
    public  static class ViewHolderProducto extends RecyclerView.ViewHolder {
        public ResizableImageView productImage;
        public TextView productNameTV;
        public TextView productPriceTV;
        private Item product;

        public ViewHolderProducto(View v, final CategoryRecyclerInterface categoryRecyclerInterface) {
            super(v);
            productNameTV = (TextView) v.findViewById(R.id.product_item_name);
            productPriceTV = (TextView) v.findViewById(R.id.product_item_price);
            productImage = (ResizableImageView) v.findViewById(R.id.product_item_image);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryRecyclerInterface.onProductSelected(v, product);
                }
            });
        }

       public void bindContent(Item product) {
            this.product = product;
        }
    }
}
