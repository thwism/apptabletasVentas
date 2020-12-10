package ibzssoft.com.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.recibir.RecibirCheques;
import ibzssoft.com.recibir.RecibirFacturas;


/**
 * Created by root on 04/12/15.
 */
public class ViewHolderCartera extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
    private String pck_id;
    private String pck_trans;
    private String pck_cli;
    private String pck_ruc;
    private String pck_com;
    private String pck_dir;
    private String pck_emi;
    private int respaldos;
    private TextView pck_cliente;
    private TextView pck_respaldos;
    private TextView pck_transaccion;
    private TextView pck_emision;
    private TextView pck_plazo;
    private TextView pck_valor_factura;
    private TextView pck_valor_cancelado;
    private TextView pck_vencimiento;
    private TextView pck_saldo_vencer,pck_saldo_vencido;
    private TextView pck_dias_vencidos;
    private Context context;
    private ItemClickListener clickListener;
    /*Headers*/

    public ViewHolderCartera(View view, Context context) {
        super(view);
            pck_respaldos= (TextView) itemView.findViewById(R.id.cheques);
            pck_cliente= (TextView) itemView.findViewById(R.id.cliente);
            pck_transaccion= (TextView) itemView.findViewById(R.id.transaccion);
            pck_emision= (TextView) itemView.findViewById(R.id.emision);
            pck_plazo= (TextView) itemView.findViewById(R.id.plazo);
            pck_valor_cancelado= (TextView) itemView.findViewById(R.id.valor_cancelado);
            pck_valor_factura= (TextView) itemView.findViewById(R.id.valor_factura);
            pck_vencimiento= (TextView) itemView.findViewById(R.id.vencimiento);
            pck_saldo_vencer= (TextView) itemView.findViewById(R.id.saldo_vencer);
            pck_saldo_vencido= (TextView) itemView.findViewById(R.id.saldo_vencido);
            pck_dias_vencidos= (TextView) itemView.findViewById(R.id.dias_vencidos);

        this.context=context;
        view.setOnClickListener(this);
        view.setOnCreateContextMenuListener(this);
    }
    public String redondearNumero(double numero){
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero).replace(",",".");
    }
    public void bind(PCKardex model) {
        pck_id=model.getIdcartera();
        pck_trans=model.getTrans();
        pck_cli=model.getNombrecli();
        pck_ruc=model.getRuc();
        pck_com=model.getComercialcli();
        pck_dir=model.getDireccioncli();
        pck_emi=model.getFechaemision();
        respaldos=model.getCantcheque();
        pck_cliente.setText(model.getNombrecli());
        pck_respaldos.setText(String.valueOf(model.getCantcheque()));
        pck_dias_vencidos.setText(String.valueOf(model.getDvencidos()));
        pck_transaccion.setText(model.getTrans());
        pck_plazo.setText(String.valueOf(model.getPlazo()));
        pck_valor_factura.setText(redondearNumero(model.getValor()));
        pck_valor_cancelado.setText(redondearNumero(model.getPagado()));
        pck_emision.setText(new ParseDates().changeDateToStringSimpleYear(new ParseDates().changeStringToDateSimple(model.getFechaemision())));
        pck_saldo_vencer.setText(redondearNumero(model.getSaldoxvence()));
        pck_saldo_vencido.setText(redondearNumero(model.getSaldovencido()));
        pck_vencimiento.setText(new ParseDates().changeDateToStringSimpleYear(new ParseDates().changeStringToDateSimple(model.getFechavenci())));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuItem1=menu.add(0, R.id.menu_cartera_ver, 1, "Ver Detalles");
        MenuItem menuItem2=menu.add(0, R.id.menu_cartera_cheques, 2, "Ver Cheques Postfechados");
        menuItem1.setOnMenuItemClickListener(this);
        menuItem2.setOnMenuItemClickListener(this);
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cartera_ver:
                RecibirFacturas recibirFacturas = new RecibirFacturas(context,pck_trans,pck_cli,pck_ruc,pck_com,pck_dir,pck_emi);
                recibirFacturas.ejecutartarea();
                break;
            case R.id.menu_cartera_cheques:
                if(respaldos!=0){
                    RecibirCheques recibirCheques= new RecibirCheques(context,pck_id,pck_trans,pck_ruc,pck_cli,pck_com,pck_dir,pck_emi);
                    recibirCheques.ejecutartarea();
                }else{
                    Toast ts= Toast.makeText(context,R.string.info_sin_cheques,Toast.LENGTH_SHORT);
                    ts.show();
                }
                break;

            default:
                break;
        }
        return true;
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    @Override
    public void onClick(View view) {
        clickListener.onClick(view, getPosition(), false);
    }
}
