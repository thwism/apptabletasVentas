package ibzssoft.com.ishidamovile.oferta.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.ResizableImageView;
import ibzssoft.com.interfaces.CartRecyclerInterface;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.listeners.OnSingleClickListener;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.cart.Cart;
import ibzssoft.com.modelo.cart.CartDiscountItem;
import ibzssoft.com.modelo.cart.CartProductItem;
import ibzssoft.com.storage.DBSistemaGestion;
import timber.log.Timber;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.interfaces.CartRecyclerInterface;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.listeners.OnSingleClickListener;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.cart.Cart;
import ibzssoft.com.modelo.cart.CartDiscountItem;
import ibzssoft.com.modelo.cart.CartProductItem;
import ibzssoft.com.storage.DBSistemaGestion;
import timber.log.Timber;

/**
 * Adapter handling list of cart items.
 */
public class OfertaRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<CartProductItem> cartProductItems = new ArrayList<>();
    private final CartRecyclerInterface cartRecyclerInterface;
    private final Context context;
    private LayoutInflater layoutInflater;

    /**
     * Creates an adapter that handles a list of cart items.
     *
     * @param context               activity context.
     * @param cartRecyclerInterface listener indicating events that occurred.
     */
    public OfertaRecyclerAdapter(Context context, CartRecyclerInterface cartRecyclerInterface) {
        this.context = context;
        this.cartRecyclerInterface = cartRecyclerInterface;
    }

    @Override
    public int getItemCount() {
        return cartProductItems.size();
    }


    private CartProductItem getCartProductItem(int position) {
        return cartProductItems.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_cart_product, parent, false);
        return new ViewHolderProduct(view, cartRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderProduct) {
            ViewHolderProduct viewHolderProduct = (ViewHolderProduct) holder;

            CartProductItem cartProductItem = getCartProductItem(position);
            viewHolderProduct.bindContent(cartProductItem);
            DBSistemaGestion helper = new DBSistemaGestion(context);
            try{
                Cursor cursor= helper.consultarItem(cartProductItem.getProductId());
                if(cursor.moveToFirst()){
                    viewHolderProduct.cartProductName.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)));
                    viewHolderProduct.cartProductPrice.setText(context.getString(R.string.dollar)+cartProductItem.getTotalItemPrice());
                    viewHolderProduct.cartProductQuantity.setText(context.getString(R.string.format_quantity, cartProductItem.getQuantity()));
                    viewHolderProduct.cartProductDetails.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_presentacion)));
                    String path= cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_img1));
                    Uri uri;
                    if(path!=null){
                        uri=Uri.fromFile(new File(path));
                    }else uri=null;
                    Picasso.with(context).load(uri)
                            .fit().centerInside()
                            .placeholder(R.drawable.placeholder_loading)
                            .error(R.drawable.placeholder_loading)
                            .into(viewHolderProduct.cartProductImage);
                }
                cursor.close();
                helper.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Timber.e(new RuntimeException(), "Unknown ViewHolder in class: %s", this.getClass().getSimpleName());
        }
    }

    public void refreshItems(Cart cart) {
        if (cart != null) {
            cartProductItems.clear();
            cartProductItems.addAll(cart.getItems());
//            cartDiscountItems.addAll(cart.getDiscounts());
            notifyDataSetChanged();
        } else {
            Timber.e("Setting cart content with null cart");
        }
    }

    public void cleatCart() {
        cartProductItems.clear();
        notifyDataSetChanged();
    }


    // Provide a reference to the views for each data item
    public static class ViewHolderDiscount extends RecyclerView.ViewHolder {

        public TextView cartDiscountName;
        public TextView cartDiscountValue;
        private CartDiscountItem cartDiscountItem;

        public ViewHolderDiscount(View itemView, final CartRecyclerInterface cartRecyclerInterface) {
            super(itemView);
            cartDiscountName = (TextView) itemView.findViewById(R.id.cart_discount_name);
            cartDiscountValue = (TextView) itemView.findViewById(R.id.cart_discount_value);
            View deleteDiscount = itemView.findViewById(R.id.cart_discount_delete);
            deleteDiscount.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    cartRecyclerInterface.onDiscountDelete(cartDiscountItem);
                }
            });
        }

        public void bindContent(CartDiscountItem cartDiscountItem) {
            this.cartDiscountItem = cartDiscountItem;
        }
    }

    // Provide a reference to the views for each data item
    public static class ViewHolderProduct extends RecyclerView.ViewHolder {

        ResizableImageView cartProductImage;
        TextView cartProductQuantity;
        TextView cartProductName;
        TextView cartProductPrice;
        TextView cartProductDetails;
        CartProductItem cartProductItem;

        public ViewHolderProduct(View itemView, final CartRecyclerInterface cartRecyclerInterface) {
            super(itemView);
            cartProductImage = (ResizableImageView) itemView.findViewById(R.id.cart_product_image);
            cartProductQuantity = (TextView) itemView.findViewById(R.id.cart_product_quantity);
            cartProductName = (TextView) itemView.findViewById(R.id.cart_product_name);
            cartProductPrice = (TextView) itemView.findViewById(R.id.cart_product_price);
            cartProductDetails = (TextView) itemView.findViewById(R.id.cart_product_details);

            View deleteProduct = itemView.findViewById(R.id.cart_product_delete);
            deleteProduct.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    cartRecyclerInterface.onProductDelete(cartProductItem);
                }
            });
            View updateProduct = itemView.findViewById(R.id.cart_product_update);
            updateProduct.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    cartRecyclerInterface.onProductUpdate(cartProductItem);
                }
            });
            itemView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    cartRecyclerInterface.onProductSelect(cartProductItem.getProductId());
                }
            });
        }

        public void bindContent(CartProductItem cartProductItem) {
            this.cartProductItem = cartProductItem;
        }
    }
}
