package ibzssoft.com.modelo.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Usuario-pc on 06/04/2017.
 */
public class Imagen implements Serializable {
    @SerializedName("cod_item")
    private String coditem;
    @SerializedName("img1")
    private String img1;
    @SerializedName("img2")
    private String img2;
    @SerializedName("img3")
    private String img3;

    public Imagen() {
    }

    public String getCoditem() {
        return coditem;
    }

    public void setCoditem(String coditem) {
        this.coditem = coditem;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img4) {
        this.img3 = img4;
    }

    @Override
    public String toString() {
        return "Imagen{" +
                "coditem='" + coditem + '\'' +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", img3='" + img3 + '\'' +
                '}';
    }
}
