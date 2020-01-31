package ibzssoft.com.dialogs;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.QuantitySpinnerAdapter;
import ibzssoft.com.ishidamovile.CONST;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.ShoppingCar;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.Shop;
import ibzssoft.com.modelo.cart.CartProductItem;
import ibzssoft.com.modelo.product.Product;
import ibzssoft.com.modelo.product.ProductQuantity;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.utils.CalculoDescuentos;
import ibzssoft.com.utils.MsgUtils;
import timber.log.Timber;

/**
 * Dialog handles update items in the shopping cart.
 */
public class UpdateCartItemDialogFragment extends DialogFragment {

    /**
     * Defined max product quantity.
     */
    private static final int QUANTITY_MAX = 100;

    private CartProductItem cartProductItem;

    //private RequestListener requestListener;

    private View dialogProgress;
    private View dialogContent;
    private Spinner quantitySpinner;

    /**
     * Creates dialog which handles update items in the shopping cart
     *
     * @param cartProductItem item in the cart, which should be updated.
     * @return new instance of dialog.
     */
    public static UpdateCartItemDialogFragment newInstance(CartProductItem cartProductItem) {
        if (cartProductItem == null) {
            Timber.e(new RuntimeException(), "Created UpdateCartItemDialogFragment with null parameters.");
            return null;
        }
        UpdateCartItemDialogFragment updateCartItemDialogFragment = new UpdateCartItemDialogFragment();
        updateCartItemDialogFragment.cartProductItem = cartProductItem;
        return updateCartItemDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.dialogFragmentAnimation);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_update_cart_item, container, false);

        dialogProgress = view.findViewById(R.id.dialog_update_cart_item_progress);
        dialogContent = view.findViewById(R.id.dialog_update_cart_item_content);
        TextView itemName = (TextView) view.findViewById(R.id.dialog_update_cart_item_title);
        itemName.setText(cartProductItem.getProductId());

        View btnSave = view.findViewById(R.id.dialog_update_cart_item_save_btn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantitySpinner != null) {
                    ProductQuantity productQuantity = (ProductQuantity) quantitySpinner.getSelectedItem();
                    Timber.d("Quantity: %s", productQuantity);
                    if (productQuantity != null) {
                        updateProductInCart(cartProductItem.getProductId(), productQuantity.getQuantity());
                    } else {
                        Timber.e(new RuntimeException(), "Cannot obtain info about edited cart item.");
                        MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.Internal_error_reload_cart_please), MsgUtils.ToastLength.SHORT);
                        dismiss();
                    }
                } else {
                    Timber.e(new NullPointerException(), "Null spinners in editing item in cart");
                    MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.Internal_error_reload_cart_please), MsgUtils.ToastLength.SHORT);
                    dismiss();
                }
            }
        });

        View btnCancel = view.findViewById(R.id.dialog_update_cart_item_cancel_btn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Set item quantity
        QuantitySpinnerAdapter adapterQuantity = new QuantitySpinnerAdapter(getActivity(), getQuantities());
        quantitySpinner = (Spinner) view.findViewById(R.id.dialog_update_cart_item_quantity_spin);
        quantitySpinner.setAdapter(adapterQuantity);

        getProductDetail(cartProductItem);
        return view;
    }

    // Prepare quantity spinner layout
    private List<ProductQuantity> getQuantities() {
        List<ProductQuantity> quantities = new ArrayList<>();
        for (int i = 1; i <= QUANTITY_MAX; i++) {
            ProductQuantity q = new ProductQuantity(i, i + "x");
            quantities.add(q);
        }
        return quantities;
    }

    private void getProductDetail(CartProductItem cartProductItem) {
        try{
            setProgressActive(true);
            DBSistemaGestion helper = new DBSistemaGestion(getContext());
            Cursor cursor = helper.consultarItem(cartProductItem.getProductId());
            if(cursor.moveToFirst()){
                cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion));
            }
            helper.close();
            setProgressActive(false);
            setSpinners();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setSpinners() {
        int selectedPosition = cartProductItem.getQuantity() - 1;
        if (selectedPosition < 0) selectedPosition = 0;
        if (selectedPosition > (quantitySpinner.getCount() - 1))
            Timber.e(new RuntimeException(), "More item quantity that can be. Quantity: %d, max: %d", (selectedPosition + 1), quantitySpinner.getCount());
        else
            quantitySpinner.setSelection(selectedPosition);
    }

    private void updateProductInCart(String productCartId,  int newQuantity) {
        try{
            if (((ShoppingCar) getActivity()).onProductUpdate(productCartId, newQuantity)){
                Shop params = (Shop)getActivity().getIntent().getSerializableExtra("shop");
                double porcentaje = new CalculoDescuentos(getContext(), params.getClienteid(), productCartId).obtenerPorcentajeDescuento();

                dismiss();
                ((ShoppingCar) getActivity()).onCartSelected();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void setProgressActive(boolean active) {
        if (active) {
            dialogProgress.setVisibility(View.VISIBLE);
            dialogContent.setVisibility(View.INVISIBLE);
        } else {
            dialogProgress.setVisibility(View.GONE);
            dialogContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        //ShoppingCar.getInstance().getRequestQueue().cancelAll(CONST.UPDATE_CART_ITEM_REQUESTS_TAG);
        super.onStop();
    }
}
